document.addEventListener("DOMContentLoaded", () => {
    const registrationForm = document.querySelector("form");

    registrationForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(registrationForm);
        const user = {
            email: formData.get("email"),
            name: formData.get("name"),
            age: formData.get("age"),
            password: formData.get("password")
        };

        try {
            const response = await fetch("/api/auth/registration", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });

            if (response.ok) {
                alert("Регистрация успешна! Теперь вы можете войти.");
                window.location.href = "/api/login"; // Перенаправление на страницу входа
            } else {
                const errorData = await response.json();
                alert(`Ошибка регистрации: ${errorData.message}`);
            }
        } catch (error) {
            console.error("Ошибка запроса:", error);
            alert("Произошла ошибка. Попробуйте позже.");
        }
    });
});
