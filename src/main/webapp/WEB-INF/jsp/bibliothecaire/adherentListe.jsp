<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Adherent" %>
<%@ page import="java.util.List" %>

<%
    List<Adherent> adherents = (List<Adherent>) request.getAttribute("adherents");
%>

<h1>Liste des Adhérents</h1>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Profil</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% if (adherents != null && !adherents.isEmpty()) { %>
            <% for (Adherent adherent : adherents) { %>
                <tr>
                    <td><%= adherent.getId() %></td>
                    <td><%= adherent.getNom() %></td>
                    <td><%= adherent.getPrenom() %></td>
                    <td><%= adherent.getIdProfil() %></td>
                    <td><button onclick="location.href='/api/adherent/detail/<%= adherent.getId() %>'">Voir</button></td>
                </tr>
            <% } %>
        <% } else { %>
            <tr><td colspan="4">Aucun adhérent trouvé.</td></tr>
        <% } %>
    </tbody>
</table>
