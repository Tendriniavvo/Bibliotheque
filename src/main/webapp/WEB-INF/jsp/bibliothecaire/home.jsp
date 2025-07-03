<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - Administration</title>
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <nav class="sidebar">
            <div class="sidebar-header">
                <h3>Administration</h3>
            </div>
            <ul class="nav-links">
                <li class="active">
                    <a href="/admin/dashboard"><i class="fas fa-home"></i> Tableau de bord</a>
                </li>
                <li>
                    <a href="/admin/users"><i class="fas fa-users"></i> Gestion Utilisateurs</a>
                </li>
                <li>
                    <a href="/admin/books"><i class="fas fa-book"></i> Gestion Livres</a>
                </li>
                <li>
                    <a href="/admin/loans"><i class="fas fa-clipboard-list"></i> Emprunts</a>
                </li>
                <li>
                    <a href="/admin/categories"><i class="fas fa-tags"></i> Catégories</a>
                </li>
                <li class="logout">
                    <a href="/logout"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
                </li>
            </ul>
        </nav>

        <main class="content">
            <header class="content-header">
                <h1>Tableau de bord</h1>
                <div class="user-info">
                    <span>Admin</span>
                    <img src="images/admin-avatar.png" alt="Admin" class="avatar">
                </div>
            </header>

            <div class="dashboard-stats">
                <div class="stat-card">
                    <i class="fas fa-users"></i>
                    <div class="stat-info">
                        <h3>Utilisateurs</h3>
                        <p>150</p>
                    </div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-book"></i>
                    <div class="stat-info">
                        <h3>Livres</h3>
                        <p>1,250</p>
                    </div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-clipboard-list"></i>
                    <div class="stat-info">
                        <h3>Emprunts actifs</h3>
                        <p>48</p>
                    </div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-clock"></i>
                    <div class="stat-info">
                        <h3>En retard</h3>
                        <p>12</p>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
