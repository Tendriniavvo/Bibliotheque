<%@page import="com.example.SpringPractice.entities.Department" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modifier un département</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Modifier un département</h2>
    <% Department department = (Department)request.getAttribute("department"); %>
    <form method="post" action="updateDept">
        <input type="hidden" name="id" value="<%= department.getId() %>"/>
        <div>
            <label>Nom:</label>
            <input type="text" name="nom" value="<%= department.getNom() %>"/>
        </div>
        <input type="submit" value="Modifier">
    </form>
    <a href="listeDept">Retour à la liste</a>
</body>
</html>
