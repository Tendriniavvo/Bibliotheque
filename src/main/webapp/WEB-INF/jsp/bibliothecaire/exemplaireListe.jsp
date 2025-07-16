<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Exemplaire" %>
<%@ page import="java.util.List" %>

<%
    List<Exemplaire> exemplaires = (List<Exemplaire>) request.getAttribute("exemplaires");
%>

<h1>Tableau de Bord - Gestion des Exemplaires</h1>
<a href="/exemplaire/form" class="add-button">Ajouter un Exemplaire</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Livre</th>
            <th>Quantité</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Exemplaire exemplaire : exemplaires) { %>
        <tr>
            <td><%= exemplaire.getId() %></td>
            <td><%= exemplaire.getLivre() != null ? exemplaire.getLivre().getTitre() : "" %></td>
            <td><%= exemplaire.getQuantite() %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/exemplaire/edit?id=<%= exemplaire.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer cet exemplaire ?')) location.href='/exemplaire/delete?id=<%= exemplaire.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
