document.addEventListener('DOMContentLoaded', async function() {
    var registerButton = document.getElementById('registerButton');
    registerButton.addEventListener('click', async function(e) {
        e.preventDefault();

        var userData = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            number: document.getElementById('phoneNumber').value,
            username: document.getElementById('username').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value,
            role: document.getElementById('role').value
        };

        if (userData.password !== userData.confirmPassword) {
            alert('Пароли не совпадают!');
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/auth/registration', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (data.success) {
                alert('Регистрация выполнена!');
                window.location.href = '/login';
            } else {
                alert('Ошибка регистрации: ' + data.message);
            }
        } catch (error) {
            console.error('Ошибка при отправке данных:', error);
        }
    });
});