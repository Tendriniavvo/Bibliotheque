<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Prolongement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>

<%
    List<Prolongement> prolongements = (List<Prolongement>) request.getAttribute("prolongements");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
%>

<h1>Tableau de Bord - Gestion des Prolongements</h1>
<a href="/prolongement/form" class="add-button">Ajouter un Prolongement</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>ID Emprunt</th>
            <th>Adherent</th>
            <th>Date Fin</th>
            <th>Date de Prolongement</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Prolongement p : prolongements) { %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getEmprunt() != null ? p.getEmprunt().getId() : "" %></td>
            <td><%= p.getEmprunt() != null ? p.getEmprunt().getAdherent().getNom() : "" %></td>
            <td><%= p.getDateFin() != null ? formatter.format(p.getDateFin()) : "" %></td>
            <td><%= p.getDateProlongement() != null ? formatter.format(p.getDateProlongement()) : "" %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/prolongement/edit?id=<%= p.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer ce prolongement ?')) location.href='/prolongement/delete?id=<%= p.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
