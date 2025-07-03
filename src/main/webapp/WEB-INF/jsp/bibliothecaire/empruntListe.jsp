<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>

<%
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
%>

<h1>Tableau de Bord - Gestion des Emprunts</h1>
<a href="/emprunt/form" class="add-button">Ajouter un Emprunt</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Adhérent</th>
            <th>Exemplaire (Livre)</th>
            <th>Type d'Emprunt</th>
            <th>Date d'Emprunt</th>
            <th>Date Retour Prévue</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Emprunt emprunt : emprunts) { %>
        <tr>
            <td><%= emprunt.getId() %></td>
            <td><%= emprunt.getAdherent() != null ? emprunt.getAdherent().getNom() + " " + emprunt.getAdherent().getPrenom() : "" %></td>
            <td>
                <%= emprunt.getExemplaire() != null && emprunt.getExemplaire().getLivre() != null 
                    ? emprunt.getExemplaire().getLivre().getTitre() : "" %>
            </td>
            <td><%= emprunt.getTypeEmprunt() != null ? emprunt.getTypeEmprunt().getNomType() : "" %></td>
            <td><%= emprunt.getDateEmprunt() != null ? formatter.format(emprunt.getDateEmprunt()) : "" %></td>
            <td><%= emprunt.getDateRetourPrevue() != null ? formatter.format(emprunt.getDateRetourPrevue()) : "" %></td>
            <td class="action-buttons">
                <button class="edit-button" onclick="location.href='/emprunt/edit?id=<%= emprunt.getId() %>'">Modifier</button>
                <button class="delete-button" onclick="if(confirm('Voulez-vous vraiment supprimer cet emprunt ?')) location.href='/emprunt/delete?id=<%= emprunt.getId() %>'">Supprimer</button>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
