async function thisUser() {
    fetch("http://localhost:8080/api/user")
        .then(res => res.json())
        .then(data => {
            // Добавляем имя пользователя в заголовок
            $('#headerUsername').append(data.name);

            // Обрабатываем роли
            let roles = data.roles.map(role => role.roleName ? " " + role.roleName : " Unknown");
            $('#headerRoles').append(roles.join(', '));

            // Формируем строку таблицы
            let user = `
    <tr>
        <td>${data.name}</td>
        <td>${data.email}</td>
        <td>${data.age}</td>
        <td>${roles.join(', ')}</td>
    </tr>`;
            $('#userPanelBody').append(user);
        })
        .catch(error => console.error("Error fetching user data:", error));
}


$(async function() {
    await thisUser();
});