<%@page import="com.example.SpringPractice.entities.Department"  %>
<%@page import="java.util.List"  %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des départements</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Liste des départements</h2>
    <a href="deptForm">Ajouter un département</a>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Actions</th>
        </tr>
        <% 
        List<Department> depts = (List<Department>)request.getAttribute("departments");
        for(Department dept: depts) { %>
            <tr>
                <td><%= dept.getId() %></td>
                <td><%= dept.getNom() %></td>
                <td>
                    <a href="updateDept?id=<%= dept.getId() %>">Modifier</a>
                    <a href="deleteDept?id=<%= dept.getId() %>">Supprimer</a>
                </td>
            </tr>
        <% } %>
    </table>
    <a href="home">Retour à l'accueil</a>
</body>
</html>
