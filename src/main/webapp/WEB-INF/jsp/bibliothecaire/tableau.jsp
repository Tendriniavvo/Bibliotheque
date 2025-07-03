            <h1>Tableau de Bord - Gestion des Livres</h1>
            <a href="add-book.html" class="add-button">Ajouter un Livre</a>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Titre</th>
                        <th>Auteur</th>
                        <th>Année</th>
                        <th>ISBN</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>Les Misérables</td>
                        <td>Victor Hugo</td>
                        <td>1862</td>
                        <td>978-0140444308</td>
                        <td class="action-buttons">
                            <button class="edit-button" onclick="location.href='edit-book.html?id=1'">Modifier</button>
                            <button class="delete-button" onclick="confirm('Voulez-vous vraiment supprimer ce livre ?')">Supprimer</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>1984</td>
                        <td>George Orwell</td>
                        <td>1949</td>
                        <td>978-0451524935</td>
                        <td class="action-buttons">
                            <button class="edit-button" onclick="location.href='edit-book.html?id=2'">Modifier</button>
                            <button class="delete-button" onclick="confirm('Voulez-vous vraiment supprimer ce livre ?')" >Supprimer</button>
                        </td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Le Petit Prince</td>
                        <td>Antoine de Saint-Exupéry</td>
                        <td>1943</td>
                        <td>978-0156013987</td>
                        <td class="action-buttons">
                            <button class="edit-button" onclick="location.href='edit-book.html?id=3'">Modifier</button>
                            <button class="delete-button" onclick="confirm('Voulez-vous vraiment supprimer ce livre ?')" >Supprimer</button>
                        </td>
                    </tr>
                </tbody>
            </table>