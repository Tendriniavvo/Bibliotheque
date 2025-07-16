<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Penalite" %>
<%@ page import="java.util.List" %>

<%
    List<Penalite> penalites = (List<Penalite>) request.getAttribute("penalites");
%>

<h1>Tableau de Bord - Gestion des Pénalités</h1>
<a href="/penalite/form" class="add-button">Ajouter une Pénalité</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nom Adhérent</th>
            <th>Date de Début</th>
            <th>Nombre de Jours</th>
            <th>Raison</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Penalite penalite : penalites) { %>
        <tr>
            <td><%= penalite.getId() %></td>
            <td><%= penalite.getAdherent().getNom() %> <%= penalite.getAdherent().getPrenom() %></td>
            <td><%= penalite.getDateDebut() %></td>
            <td><%= penalite.getJour() %></td>
            <td><%= penalite.getRaison() != null ? penalite.getRaison() : "" %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/penalite/edit?id=<%= penalite.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer cette pénalité ?')) location.href='/penalite/delete?id=<%= penalite.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
