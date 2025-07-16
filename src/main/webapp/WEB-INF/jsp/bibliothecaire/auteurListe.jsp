<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Auteur" %>
<%@ page import="java.util.List" %>

<%
    List<Auteur> auteurs = (List<Auteur>) request.getAttribute("auteurs");
%>

<h1>Tableau de Bord - Gestion des Auteurs</h1>
<a href="/auteur/form" class="add-button">Ajouter un Auteur</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Auteur auteur : auteurs) { %>
        <tr>
            <td><%= auteur.getId() %></td>
            <td><%= auteur.getNom() %></td>
            <td><%= auteur.getPrenom() != null ? auteur.getPrenom() : "" %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/auteur/edit?id=<%= auteur.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer cet auteur ?')) location.href='/auteur/delete?id=<%= auteur.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
