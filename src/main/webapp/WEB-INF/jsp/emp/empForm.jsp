<%@page import="com.example.SpringPractice.entities.Department" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ajouter un employé</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Ajouter un employé</h2>
    <form method="post" action="saveEmp">
        <div>
            <label>Nom:</label>
            <input type="text" name="nom_emp"/>
        </div>
        <div>
            <label>Numéro:</label>
            <input type="text" name="num_emp"/>
        </div>
        <div>
            <label>Date de naissance:</label>
            <input type="date" name="birth"/>
        </div>
        <div>
            <label>Département:</label>
            <select name="id_dept">
                <% 
                List<Department> departments = (List<Department>)request.getAttribute("departments");
                for(Department dept: departments) { %>
                    <option value="<%= dept.getId() %>"><%= dept.getNom() %></option>
                <% } %>
            </select>
        </div>
        <input type="submit" value="Enregistrer">
    </form>
    <a href="listeEmp">Retour à la liste</a>
</body>
</html>
