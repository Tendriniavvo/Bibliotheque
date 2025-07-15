<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<h1>Mon Tableau de bord</h1>
<div style="display: flex; gap: 30px; flex-wrap: wrap;">
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Emprunts en cours</h2>
        <p style="font-size: 2em; color: #3498db;">${nbEmpruntsEnCours}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Réservations</h2>
        <p style="font-size: 2em; color: #27ae60;">${nbReservations}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Pénalités actives</h2>
        <p style="font-size: 2em; color: #c0392b;">${nbPenalitesActives}</p>
    </div>
    <div style="background: #f5f5f5; padding: 20px; border-radius: 8px; min-width: 200px;">
        <h2>Abonnement actif</h2>
        <p style="font-size: 2em; color: #16a085;">${abonnementActif ? "Oui" : "Non"}</p>
    </div>
</div> 