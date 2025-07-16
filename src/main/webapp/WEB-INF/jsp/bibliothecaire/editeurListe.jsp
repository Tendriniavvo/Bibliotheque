<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Editeur" %>
<%@ page import="java.util.List" %>

<%
    List<Editeur> editeurs = (List<Editeur>) request.getAttribute("editeurs");
%>

<h1>Tableau de Bord - Gestion des Éditeurs</h1>
<a href="/editeur/form" class="add-button">Ajouter un Éditeur</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Editeur editeur : editeurs) { %>
        <tr>
            <td><%= editeur.getId() %></td>
            <td><%= editeur.getNom() %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/editeur/edit?id=<%= editeur.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer cet éditeur ?')) location.href='/editeur/delete?id=<%= editeur.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
