<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Reservation" %>
<%@ page import="java.util.List" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
%>

<h1>Gestion des Réservations</h1>
<a href="/reservation/form" class="add-button">Ajouter une Réservation</a>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Livre</th>
            <th>Adhérent</th>
            <th>Date Demande</th>
            <th>Date à reserver</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <% for (Reservation reservation : reservations) { %>
            <tr>
                <td><%= reservation.getId() %></td>
                <td><%= reservation.getLivre() != null ? reservation.getLivre().getTitre() : "" %></td>
                <td><%= reservation.getAdherent() != null ? reservation.getAdherent().getNom() : "" %></td>
                <td><%= reservation.getDateDemande() != null ? reservation.getDateDemande() : "" %></td>
                <td><%= reservation.getDateAReserver() != null ? reservation.getDateAReserver() : "" %></td>
                <td>
                    <button onclick="location.href='/reservation/edit?id=<%= reservation.getId() %>'">Modifier</button>
                    <button onclick="if(confirm('Voulez-vous vraiment supprimer cette réservation ?')) location.href='/reservation/delete?id=<%= reservation.getId() %>'">Supprimer</button>
                </td>
            </tr>
        <% } %>
    </tbody>
</table>
