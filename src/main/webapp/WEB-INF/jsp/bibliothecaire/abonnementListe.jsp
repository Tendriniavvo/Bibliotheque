<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Abonnement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Optional" %>
<%
    List<Abonnement> abonnements = (List<Abonnement>) request.getAttribute("abonnements");
%>
<h1>Tableau de Bord - Gestion des Abonnements</h1>
<a href="/abonnement/form" class="add-button">Ajouter un Abonnement</a>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nom Adhérent</th>
            <th>Date de Début</th>
            <th>Date de Fin</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Abonnement abonnement : abonnements ) { %>
        <tr>
            <td><%= abonnement.getId() %></td>
            <td><%= abonnement.getAdherent().getNom() %> <%= abonnement.getAdherent().getPrenom() %></td>
            <td><%= abonnement.getDateDebut() %></td>
            <td><%= abonnement.getDateFin() %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='edit-abonnement.html?id=<%= abonnement.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="confirm('Voulez-vous vraiment supprimer cet abonnement ?')">Supprimer</button>
            </td>
        <% } %>
    </tbody>
</table>
