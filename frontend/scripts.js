document.addEventListener('DOMContentLoaded', function () {
  const BACKEND_URL = 'http://localhost:8080';
  const loginButton = document.getElementById('loginButton');
  const logoutButton = document.getElementById('logoutButton');
  const statusEl = document.getElementById('status');
  const loginSection = document.getElementById('loginSection');
  const userSection = document.getElementById('userSection');
  const userName = document.getElementById('userName');
  const userEmail = document.getElementById('userEmail');
  const userAvatar = document.getElementById('userAvatar');
  const profileImage = document.getElementById('profileImage');

  if (loginButton) {
    loginButton.addEventListener('click', function () {
      try { sessionStorage.setItem('preAuthPage', window.location.href); } catch (e) {}
      window.location.href = BACKEND_URL + '/oauth2/authorization/google';
    });
  }

  if (logoutButton) {
    logoutButton.addEventListener('click', function () {
      // Navigate to /logout so browser follows redirects (avoids fetch/CORS issues)
      window.location.href = BACKEND_URL + '/logout';
    });
  }

  function setDisplay(el, val) { if (el) el.style.display = val; }
  function setSrc(el, src) { if (el) el.src = src; }

  function renderLoggedIn(user) {
    if (statusEl) statusEl.textContent = 'Signed in successfully';
    setDisplay(loginSection, 'none');
    setDisplay(userSection, 'block');
    setDisplay(logoutButton, 'inline-block');

    const name = user.name || 'Google User';
    const email = user.email || 'No email provided';
    const avatar = user.picture || 'https://sweng861-bucket.s3.us-east-1.amazonaws.com/Self-Portrait.jpg';

    if (userName) userName.textContent = name;
    if (userEmail) userEmail.textContent = email;
    setSrc(userAvatar, avatar);
    setSrc(profileImage, avatar);
    setDisplay(profileImage, 'block');
  }

  fetch(BACKEND_URL + '/api/user', {
    method: 'GET',
    credentials: 'include'
  })
    .then(function (response) {
      if (!response.ok) {
        throw new Error('Not authenticated');
      }
      return response.json();
    })
    .then(function (data) {
      if (data.authenticated) {
        renderLoggedIn(data);
        try {
          const pre = sessionStorage.getItem('preAuthPage');
          if (pre && pre !== window.location.href) {
            sessionStorage.removeItem('preAuthPage');
            window.location.href = pre;
            return;
          }
        } catch (e) {}
      } else {
        if (statusEl) statusEl.textContent = 'Not logged in';
      }
    })
    .catch(function () {
      if (statusEl) statusEl.textContent = 'Not logged in';
      setDisplay(loginSection, 'block');
      setDisplay(userSection, 'none');
      setDisplay(profileImage, 'none');
      setDisplay(logoutButton, 'none');
      try {
        const pre = sessionStorage.getItem('preAuthPage');
        if (pre && pre !== window.location.href) {
          sessionStorage.removeItem('preAuthPage');
          window.location.href = pre;
        }
      } catch (e) {}
    });
});
