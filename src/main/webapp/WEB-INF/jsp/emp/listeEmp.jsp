<%@page import="com.example.SpringPractice.entities.Employe" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des employés</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Liste des employés</h2>
    <a href="empForm">Ajouter un employé</a>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Numéro</th>
            <th>Date de naissance</th>
            <th>Département</th>
            <th>Actions</th>
        </tr>
        <% 
        List<Employe> employes = (List<Employe>)request.getAttribute("employes");
        for(Employe emp: employes) { %>
            <tr>
                <td><%= emp.getId_emp() %></td>
                <td><%= emp.getNom_emp() %></td>
                <td><%= emp.getNum_emp() %></td>
                <td><%= emp.getBirth() %></td>
                <td><%= emp.getId_dept() %></td>
                <td>
                    <a href="updateEmp?id=<%= emp.getId_emp() %>">Modifier</a>
                    <a href="deleteEmp?id=<%= emp.getId_emp() %>">Supprimer</a>
                </td>
            </tr>
        <% } %>
    </table>
</body>
</html>
