<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Livre" %>
<%@ page import="java.util.List" %>

<%
    List<Livre> livres = (List<Livre>) request.getAttribute("livres");
%>

<h1>Tableau de Bord - Gestion des Livres</h1>
<a href="/livre/form" class="add-button">Ajouter un Livre</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Titre</th>
            <th>ISBN</th>
            <th>Année</th>
            <th>Éditeur</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Livre livre : livres) { %>
        <tr>
            <td><%= livre.getId() %></td>
            <td><%= livre.getTitre() %></td>
            <td><%= livre.getIsbn() != null ? livre.getIsbn() : "" %></td>
            <td><%= livre.getAnneePublication() != null ? livre.getAnneePublication() : "" %></td>
            <td><%= livre.getEditeur() != null ? livre.getEditeur().getNom() : "" %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/livre/edit?id=<%= livre.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer ce livre ?')) location.href='/livre/delete?id=<%= livre.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
