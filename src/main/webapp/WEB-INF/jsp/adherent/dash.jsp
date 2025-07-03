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