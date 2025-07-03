            <h1>Ajouter un Nouveau Livre</h1>
            <div class="form-group">
                <label for="title">Titre</label>
                <input type="text" id="title" name="title" required>
            </div>
            <div class="form-group">
                <label for="author">Auteur</label>
                <select id="author" name="author" required>
                    <option value="" disabled selected>Sélectionner un auteur</option>
                    <option value="Victor Hugo">Victor Hugo</option>
                    <option value="George Orwell">George Orwell</option>
                    <option value="Antoine de Saint-Exupéry">Antoine de Saint-Exupéry</option>
                    <option value="J.K. Rowling">J.K. Rowling</option>
                    <option value="Autres">Autres</option>
                </select>
            </div>
            <div class="form-group">
                <label for="year">Année</label>
                <input type="number" id="year" name="year" required>
            </div>
            <div class="form-group">
                <label for="genre">Genre</label>
                <select id="genre" name="genre" required>
                    <option value="" disabled selected>Sélectionner un genre</option>
                    <option value="Roman">Roman</option>
                    <option value="Science-Fiction">Science-Fiction</option>
                    <option value="Fantaisie">Fantaisie</option>
                    <option value="Biographie">Biographie</option>
                    <option value="Poésie">Poésie</option>
                </select>
            </div>
            <div class="form-group">
                <label for="condition">État</label>
                <select id="condition" name="condition" required>
                    <option value="" disabled selected>Sélectionner un état</option>
                    <option value="Neuf">Neuf</option>
                    <option value="Bon">Bon</option>
                    <option value="Usagé">Usagé</option>
                    <option value="Endommagé">Endommagé</option>
                </select>
            </div>
            <div class="form-group">
                <label for="isbn">ISBN</label>
                <input type="text" id="isbn" name="isbn" required>
            </div>
            <button type="submit">Ajouter</button>
            <a href="index.html"><button type="button" class="back-button">Retour</button></a>