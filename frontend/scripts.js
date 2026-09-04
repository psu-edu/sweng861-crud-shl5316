document.addEventListener('DOMContentLoaded', function () {
  const BACKEND_URL = 'http://localhost:8080';
  const loginButton = document.getElementById('loginButton');
  const statusEl = document.getElementById('status');
  const loginSection = document.getElementById('loginSection');
  const userSection = document.getElementById('userSection');
  const userName = document.getElementById('userName');
  const userEmail = document.getElementById('userEmail');
  const userAvatar = document.getElementById('userAvatar');
  const profileImage = document.getElementById('profileImage');

  if (loginButton) {
    loginButton.addEventListener('click', function () {
      window.location.href = BACKEND_URL + '/oauth2/authorization/google';
    });
  }

  function renderLoggedIn(user) {
    statusEl.textContent = 'Signed in successfully';
    loginSection.style.display = 'none';
    userSection.style.display = 'block';

    const name = user.name || 'Google User';
    const email = user.email || 'No email provided';
    const avatar = user.picture || 'https://sweng861-bucket.s3.us-east-1.amazonaws.com/Self-Portrait.jpg';

    userName.textContent = name;
    userEmail.textContent = email;
    userAvatar.src = avatar;
    profileImage.src = avatar;
    profileImage.style.display = 'block';
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
      } else {
        statusEl.textContent = 'Not logged in';
      }
    })
    .catch(function () {
      statusEl.textContent = 'Not logged in';
      loginSection.style.display = 'block';
      userSection.style.display = 'none';
      profileImage.style.display = 'none';
    });
});
