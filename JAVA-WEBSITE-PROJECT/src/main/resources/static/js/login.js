document.addEventListener('DOMContentLoaded', async function() {
    var loginButton = document.getElementById('loginButton');

    if (loginButton) {
        loginButton.addEventListener('click', async function() {
            var formData = {
                username: document.getElementById('username').value,
                password: document.getElementById('password').value,
            };

            try {
                const response = await fetch('http://localhost:8081/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(formData)
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok.');
                }

                const data = await response.json();

                if (data.result === true && data.accessToken) {
                    localStorage.setItem('userId', data.userId)
                    localStorage.setItem('accessToken', data.accessToken);
                    localStorage.setItem('refreshToken', data.refreshToken);
                    console.log('Login successful');
                    window.location.href = '/profile';
                } else {
                    console.error('Login failed');
                }
            } catch (error) {
                console.error('Error during login process:', error);
            }
        });
    }
});