<%@page import="com.example.SpringPractice.entities.*" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un employé</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Modifier un employé</h2>
    <% Employe employe = (Employe)request.getAttribute("employe"); %>
    <form method="post" action="updateEmp">
        <input type="hidden" name="id_emp" value="<%= employe.getId_emp() %>"/>
        <div>
            <label>Nom:</label>
            <input type="text" name="nom_emp" value="<%= employe.getNom_emp() %>" required/>
        </div>
        <div>
            <label>Numéro:</label>
            <input type="text" name="num_emp" value="<%= employe.getNum_emp() %>" required/>
        </div>
        <div>
            <label>Date de naissance:</label>
            <input type="date" name="birth" value="<%= employe.getBirth() %>" required/>
        </div>
        <div>
            <label>Département:</label>
            <select name="id_dept" required>
                <% 
                List<Department> departments = (List<Department>)request.getAttribute("departments");
                for(Department dept: departments) { %>
                    <option value="<%= dept.getId() %>" 
                            <%= (dept.getId() == employe.getId_dept() ? "selected" : "") %>>
                        <%= dept.getNom() %>
                    </option>
                <% } %>
            </select>
        </div>
        <input type="submit" value="Modifier">
    </form>
    <a href="listeEmp">Retour à la liste</a>
</body>
</html>
