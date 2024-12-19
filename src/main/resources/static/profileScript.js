async function thisUser() {
    try {
        const response = await fetch("http://localhost:8080/api/user");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // Добавляем имя пользователя в заголовок
        $('#headerUsername').append(data.name);

        // Обрабатываем роли
        const roles = data.roles; // Роли уже в виде списка строк
        $('#headerRoles').append(roles.join(', '));

        // Формируем строку таблицы
        const userRow = `
            <tr>
                <td>${data.name}</td>
                <td>${data.email}</td>
                <td>${data.age}</td>
                <td>${roles.join(', ')}</td>
            </tr>`;
        $('#userPanelBody').append(userRow);
    } catch (error) {
        console.error("Error fetching user data:", error);
    }
}

// Инициализация при загрузке страницы
$(async function() {
    await thisUser();
});
