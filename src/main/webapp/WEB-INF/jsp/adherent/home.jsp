<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Client - Bibliothèque</title>
    <link rel="stylesheet" href="css/adherent.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="client-container">
        <nav class="sidebar">
            <div class="user-profile">
                <img src="images/default-avatar.png" alt="Photo de profil" class="profile-img">
                <h3>Bienvenue, ${user.prenom}</h3>
            </div>
            <ul class="nav-links">
                <li class="active">
                    <a href="/adherent/dashboard"><i class="fas fa-home"></i>Accueil</a>
                </li>
                <li>
                    <a href="/adherent/catalogue"><i class="fas fa-book"></i>Catalogue</a>
                </li>
                <li>
                    <a href="/adherent/emprunts"><i class="fas fa-list"></i>Mes Emprunts</a>
                </li>
                <li>
                    <a href="/adherent/reservations"><i class="fas fa-bookmark"></i>Mes Réservations</a>
                </li>
                <li>
                    <a href="/adherent/profile"><i class="fas fa-user"></i>Mon Profil</a>
                </li>
                <li class="logout">
                    <a href="/logout"><i class="fas fa-sign-out-alt"></i>Déconnexion</a>
                </li>
            </ul>
        </nav>

        <main class="content">
            <header class="welcome-header">
                <h1>Mon Espace</h1>
                <div class="search-bar">
                    <input type="text" placeholder="Rechercher un livre...">
                    <button><i class="fas fa-search"></i></button>
                </div>
            </header>

            <div class="quick-stats">
                <div class="stat-box">
                    <i class="fas fa-book-reader"></i>
                    <div class="stat-details">
                        <h3>Emprunts en cours</h3>
                        <p>${empruntsEnCours}</p>
                    </div>
                </div>
                <div class="stat-box">
                    <i class="fas fa-clock"></i>
                    <div class="stat-details">
                        <h3>À rendre bientôt</h3>
                        <p>${prochainesEcheances}</p>
                    </div>
                </div>
                <div class="stat-box">
                    <i class="fas fa-bookmark"></i>
                    <div class="stat-details">
                        <h3>Réservations</h3>
                        <p>${reservationsEnCours}</p>
                    </div>
                </div>
            </div>

            <section class="recent-books">
                <h2>Livres recommandés</h2>
                <div class="books-grid">
                    <!-- Les livres seront injectés dynamiquement ici -->
                </div>
            </section>
        </main>
    </div>
</body>
</html>
