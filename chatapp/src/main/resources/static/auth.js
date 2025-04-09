const BASE_URL = 'http://localhost:8080';

// UI Elements
const loginToggle = document.getElementById('loginToggle');
const signupToggle = document.getElementById('signupToggle');
const loginForm = document.getElementById('loginForm');
const signupForm = document.getElementById('signupForm');
const loginBtn = document.getElementById('loginBtn');
const signupBtn = document.getElementById('signupBtn');
const alertBox = document.getElementById('alertBox');

// Toggle Forms
loginToggle.addEventListener('click', () => toggleForm('login'));
signupToggle.addEventListener('click', () => toggleForm('signup'));

function toggleForm(formType) {
  if (formType === 'login') {
    loginToggle.classList.add('active');
    signupToggle.classList.remove('active');
    loginForm.classList.remove('hidden');
    signupForm.classList.add('hidden');
  } else {
    signupToggle.classList.add('active');
    loginToggle.classList.remove('active');
    signupForm.classList.remove('hidden');
    loginForm.classList.add('hidden');
  }
}

// Show Alert
function showAlert(message, type) {
  alertBox.textContent = message;
  alertBox.className = `alert ${type}`;
  alertBox.classList.remove('hidden');
  setTimeout(() => alertBox.classList.add('hidden'), 3000);
}

// Handle Login
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('loginUsername').value;
  const password = document.getElementById('loginPassword').value;

  // Show loading
  loginBtn.disabled = true;
  loginBtn.querySelector('.btn-text').textContent = 'Logging in...';
  loginBtn.querySelector('.loading-icon').style.display = 'inline-block';

  try {
    const res = await fetch(`${BASE_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (res.ok) {
      const data = await res.json();
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', username);
      window.location.href = 'dashboard.html';
    } else {
      const error = await res.text();
      showAlert(`Login failed: ${error}`, 'error');
    }
  } catch (err) {
    showAlert(`Network error: ${err.message}`, 'error');
  } finally {
    // Reset button
    loginBtn.disabled = false;
    loginBtn.querySelector('.btn-text').textContent = 'Login';
    loginBtn.querySelector('.loading-icon').style.display = 'none';
  }
});

// Handle Signup (similar to login)
signupForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('signupUsername').value;
  const password = document.getElementById('signupPassword').value;

  signupBtn.disabled = true;
  signupBtn.querySelector('.btn-text').textContent = 'Signing up...';
  signupBtn.querySelector('.loading-icon').style.display = 'inline-block';

  try {
    const res = await fetch(`${BASE_URL}/api/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (res.ok) {
      showAlert('Account created! Please login.', 'success');
      toggleForm('login');
    } else {
      const error = await res.text();
      showAlert(`Signup failed: ${error}`, 'error');
    }
  } catch (err) {
    showAlert(`Network error: ${err.message}`, 'error');
  } finally {
    signupBtn.disabled = false;
    signupBtn.querySelector('.btn-text').textContent = 'Sign Up';
    signupBtn.querySelector('.loading-icon').style.display = 'none';
  }
});