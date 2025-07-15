<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bibliotheque.entities.Emprunt" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%
    List<Emprunt> emprunts = (List<Emprunt>) request.getAttribute("emprunts");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
%>
<h1>Mes Emprunts</h1>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
        <tr>
            <th>ID</th>
            <th>Livre</th>
            <th>Date d'emprunt</th>
            <th>Date retour prévue</th>
            <th>Statut</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <% if (emprunts != null && !emprunts.isEmpty()) { %>
            <% for (Emprunt emprunt : emprunts) { %>
                <tr>
                    <td><%= emprunt.getId() %></td>
                    <td><%= emprunt.getExemplaire() != null && emprunt.getExemplaire().getLivre() != null ? emprunt.getExemplaire().getLivre().getTitre() : "N/A" %></td>
                    <td><%= emprunt.getDateEmprunt() != null ? formatter.format(emprunt.getDateEmprunt()) : "" %></td>
                    <td><%= emprunt.getDateRetourPrevue() != null ? formatter.format(emprunt.getDateRetourPrevue()) : "" %></td>
                    <td><%= request.getAttribute("statuts") != null && ((java.util.Map<Integer, String>)request.getAttribute("statuts")).get(emprunt.getId()) != null ? ((java.util.Map<Integer, String>)request.getAttribute("statuts")).get(emprunt.getId()) : "En cours" %></td>
                    <td><a href="/adherent/emprunt/detail?id=<%= emprunt.getId() %>">Voir</a></td>
                </tr>
            <% } %>
        <% } else { %>
            <tr><td colspan="6">Aucun emprunt trouvé.</td></tr>
        <% } %>
    </tbody>
</table> 