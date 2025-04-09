let currentUser = localStorage.getItem('username');
let selectedUser = null;
let stompClient = null;
let typingTimer = null;

// Redirect if not logged in
if (!localStorage.getItem('token')) {
  window.location.href = "login.html";
}

// Connect WebSocket
function connectWebSocket() {
  const socket = new SockJS("http://localhost:8080/chat");
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log("Connected: " + frame);

    // Enable send button
    document.getElementById('sendBtn').disabled = false;

    // Subscribe to private messages
    stompClient.subscribe('/user/' + currentUser + '/queue/messages', function (message) {
      const msg = JSON.parse(message.body);

      if (msg.sender === selectedUser) {
        addMessageToChat(msg.sender, msg.content, false);
      } else {
        // Update unread count
        updateUnreadCount(msg.sender);
      }
    });

    // Subscribe to typing notifications
    stompClient.subscribe('/topic/typing', function (notification) {
      const data = JSON.parse(notification.body);
      if (data.sender === selectedUser && data.receiver === currentUser) {
        showTypingIndicator(data.isTyping);
      }
    });
  });
}

// Send typing notification
function sendTypingEvent(isTyping) {
  if (!stompClient || !selectedUser) return;

  const notification = {
    sender: currentUser,
    receiver: selectedUser,
    isTyping: isTyping
  };

  stompClient.send("/app/typing", {}, JSON.stringify(notification));
}

// Show/hide typing indicator
function showTypingIndicator(show) {
  const indicator = document.getElementById('typingIndicator');
  if (show) {
    indicator.classList.remove('hidden');
  } else {
    indicator.classList.add('hidden');
  }
}

// Update unread message count
function updateUnreadCount(sender) {
  const userItems = document.querySelectorAll('#userList li');
  userItems.forEach(item => {
    if (item.dataset.username === sender) {
      let badge = item.querySelector('.badge') || document.createElement('span');
      if (!badge.classList.contains('badge')) {
        badge.className = 'badge';
        item.appendChild(badge);
      }
      badge.textContent = parseInt(badge.textContent || '0') + 1;
    }
  });
}

// Send message
function sendMessage() {
  const messageInput = document.getElementById('messageInput');
  const content = messageInput.value.trim();

  if (!content || !selectedUser) return;

  const message = {
    sender: currentUser,
    receiver: selectedUser,
    content: content,
    roomId: currentUser + "_" + selectedUser,
    timestamp: new Date().toISOString()
  };

  stompClient.send("/app/private-message", {}, JSON.stringify(message));
  addMessageToChat(currentUser, content, true);
  messageInput.value = '';
}

// Add message to chat UI
function addMessageToChat(sender, content, isSent) {
  const chatBox = document.getElementById('chatBox');

  // Remove welcome message if it exists
  const welcomeMsg = chatBox.querySelector('.welcome-message');
  if (welcomeMsg) welcomeMsg.remove();

  const messageDiv = document.createElement('div');
  messageDiv.className = `message ${isSent ? 'sent' : 'received'}`;

  const now = new Date();
  const timeString = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

  messageDiv.innerHTML = `
    <p>${content}</p>
    <span class="time">${timeString}</span>
  `;

  chatBox.appendChild(messageDiv);
  chatBox.scrollTop = chatBox.scrollHeight;

  // Clear typing indicator
  showTypingIndicator(false);
}

// Load chat history
async function loadChatHistory() {
  if (!selectedUser) return;

  const chatBox = document.getElementById('chatBox');
  chatBox.innerHTML = '';

  // get this before api call
    console.log("Current user (localStorage):", localStorage.getItem('username'));
    console.log("Selected user:", selectedUser);
    console.log("Authorization token:", localStorage.getItem('token'));
  try {
    const response = await fetch(`http://localhost:8080/api/messages/history/users/${currentUser}/${selectedUser}`, {
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    });

    if (response.ok) {
      const messages = await response.json();
      messages.forEach(msg => {
        addMessageToChat(msg.sender, msg.content, msg.sender === currentUser);
      });
    }
  } catch (error) {
    console.error("Error loading messages:", error);
  }
}

// DOM Ready
document.addEventListener('DOMContentLoaded', async () => {
  // Set current username in profile
  document.getElementById('profileUsername').textContent = currentUser;

  // Load user list
  try {
    const response = await fetch('http://localhost:8080/api/users', {
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    });

    if (response.ok) {
      const users = await response.json();
      const userList = document.getElementById('userList');

      users.forEach(user => {
        if (user.username !== currentUser) {
          const li = document.createElement('li');
          li.dataset.username = user.username;
          li.innerHTML = `
            <div class="avatar">
              <img src="https://ui-avatars.com/api/?name=${user.username}" alt="${user.username}">
            </div>
            <div class="user-info">
              <h4>${user.username}</h4>
              <p>Last seen recently</p>
            </div>
          `;

          li.addEventListener('click', () => {
            // Mark as active
            document.querySelectorAll('#userList li').forEach(item => {
              item.classList.remove('active');
            });
            li.classList.add('active');

            // Clear unread badge
            const badge = li.querySelector('.badge');
            if (badge) badge.remove();

            // Set selected user
            selectedUser = user.username;
            document.getElementById('chatWith').textContent = user.username;
            loadChatHistory();
          });

          userList.appendChild(li);
        }
      });
    }
  } catch (error) {
    console.error("Error fetching users:", error);
  }

  // Connect WebSocket
  connectWebSocket();

  // Message form submission
  document.getElementById('messageForm').addEventListener('submit', (e) => {
    e.preventDefault();
    sendMessage();
  });

  // Typing detection
  document.getElementById('messageInput').addEventListener('input', () => {
    // Send typing start
    sendTypingEvent(true);

    // Clear previous timer
    if (typingTimer) clearTimeout(typingTimer);

    // Set timer to send typing stop after 2 seconds
    typingTimer = setTimeout(() => {
      sendTypingEvent(false);
    }, 2000);
  });

  // Logout
  document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = 'login.html';
  });
});


