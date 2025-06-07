<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Utente</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            color: #212529;
            line-height: 1.6;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        header {
            background-color: white;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            padding: 1.5rem 5%;
            position: relative;
            overflow: hidden;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #4CAF50, #8BC34A, #4CAF50);
            z-index: 1;
        }

        h1 {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 600;
            color: #212529;
        }

        h2 {
            color: #212529;
            font-size: 1.5rem;
            margin: 2rem 0 1.5rem;
            font-weight: 600;
            position: relative;
            padding-left: 1rem;
        }

        h2::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            height: 100%;
            width: 4px;
            border-radius: 2px;
            background-color: #4CAF50;
        }

        h3 {
            margin: 0 0 1rem 0;
            font-size: 1.25rem;
            color: #212529;
            font-weight: 600;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
            display: flex;
            gap: 0.8rem;
            margin: 0;
        }

        nav ul li a {
            display: inline-block;
            text-decoration: none;
            padding: 0.5rem 1rem;
            background-color: #4CAF50;
            color: white;
            border-radius: 6px;
            font-weight: 500;
            transition: all 0.2s ease;
            box-shadow: 0 2px 8px rgba(76, 175, 80, 0.25);
        }

        nav ul li a:hover {
            background-color: #388e3c;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.35);
        }

        main {
            flex: 1;
            max-width: 1200px;
            width: 90%;
            margin: 0 auto;
            padding: 2rem 0;
        }

        .eventi-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1.5rem;
            margin-top: 1.5rem;
        }

        .evento-card {
            background-color: white;
            border-radius: 10px;
            padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
            transition: transform 0.2s, box-shadow 0.2s;
            position: relative;
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }

        .evento-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, #4CAF50, #8BC34A);
        }

        .evento-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }

        .evento-card p {
            margin: 0.5rem 0;
            font-size: 0.95rem;
        }

        .evento-card .descrizione {
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #e9ecef;
        }

        .btn-iscrizione {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 0.6rem 1rem;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.2s ease;
            font-size: 0.85rem;
            margin-top: auto;
            box-shadow: 0 2px 5px rgba(76, 175, 80, 0.2);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            width: 100%;
            margin-bottom: 0.5rem;
        }

        .btn-iscrizione:hover {
            background-color: #388e3c;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3);
        }

        .btn-calendar {
            background-color: #2196F3;
            color: white;
            border: none;
            padding: 0.6rem 1rem;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.2s ease;
            font-size: 0.85rem;
            box-shadow: 0 2px 5px rgba(33, 150, 243, 0.2);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            width: 100%;
        }

        .btn-calendar:hover:not(:disabled) {
            background-color: #1976D2;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(33, 150, 243, 0.3);
        }

        .btn-calendar:disabled {
            background-color: #ccc;
            cursor: not-allowed;
            opacity: 0.7;
            box-shadow: none;
        }

        footer {
            text-align: center;
            padding: 1.5rem;
            background-color: white;
            border-top: 1px solid #e9ecef;
            color: #6c757d;
            margin-top: auto;
        }

        .alert {
            padding: 1rem 1.5rem;
            margin-bottom: 1.5rem;
            border-radius: 6px;
            font-weight: 500;
        }

        .alert-success {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
        }

        /* Filter form styling */
        section.eventi-disponibili form {
            background-color: white;
            padding: 1.5rem;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
            margin-bottom: 2rem;
        }

        section.eventi-disponibili label {
            font-weight: 600;
            color: #495057;
        }

        section.eventi-disponibili select {
            padding: 0.5rem 1rem;
            border: 1px solid #ced4da;
            border-radius: 6px;
            background-color: white;
            min-width: 200px;
            font-size: 0.95rem;
        }

        section.eventi-disponibili button[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 0.5rem 1.5rem;
            border-radius: 6px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        section.eventi-disponibili button[type="submit"]:hover {
            background-color: #388e3c;
            transform: translateY(-2px);
        }

        /* Empty state */
        section p:first-of-type:not(.descrizione) {
            background-color: white;
            padding: 1.5rem;
            text-align: center;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
            color: #6c757d;
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .evento-card {
            animation: fadeIn 0.3s ease-out forwards;
        }

        /* Responsive design */
        @media (max-width: 768px) {
            header {
                flex-direction: column;
                align-items: flex-start;
            }

            nav ul {
                margin-top: 1rem;
            }

            section.eventi-disponibili form {
                flex-direction: column;
                align-items: flex-start;
            }

            section.eventi-disponibili select,
            section.eventi-disponibili button[type="submit"] {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<header>
    <h1>Benvenuto, ${user.username}!</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/homeutente">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
        </ul>
    </nav>
</header>

<main>
    <!-- Success/Error Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success">
            Iscrizione effettuata con successo!
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">
                ${param.error}
        </div>
    </c:if>

    <section class="eventi-iscritti">
        <h2>I tuoi eventi</h2>
        <c:choose>
            <c:when test="${empty eventiIscritti}">
                <p>Non sei iscritto a nessun evento.</p>
            </c:when>
            <c:otherwise>
                <div class="eventi-container">
                    <c:forEach var="evento" items="${eventiIscritti}">
                        <div class="evento-card">
                            <h3>${evento.nome}</h3>
                            <p><strong>Data:</strong> ${evento.data}</p>
                            <p><strong>Ora:</strong> ${evento.ora}</p>
                            <p><strong>Luogo:</strong> ${evento.luogo}</p>
                            <p><strong>Organizzatore:</strong> ${evento.organizzatore.username}</p>
                            <p><strong>Categoria:</strong> ${evento.categoria.nome}</p>
                            <p class="descrizione"><strong>Descrizione:</strong> ${evento.descrizione}</p>
                            <form action="${pageContext.request.contextPath}/eliminaIscrizione" method="post">
                                <input type="hidden" name="eventoId" value="${evento.id}">
                                <button type="submit" class="btn-iscrizione">Cancella iscrizione</button>
                            </form>
                            <button class="btn-calendar" data-evento-id="${evento.id}" data-is-registered="true">
                                <i class="fas fa-calendar-plus"></i> Aggiungi a Google Calendar
                            </button>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="eventi-disponibili">
        <h2>Eventi disponibili</h2>
        <form method="get" action="${pageContext.request.contextPath}/homeutente">
            <label for="categoria">Filtra per categoria:</label>
            <select name="categoriaId" id="categoria">
                <option value="">Tutte</option>
                <c:forEach var="cat" items="${categorie}">
                    <option value="${cat.id}" <c:if test="${param.categoriaId == cat.id}">selected</c:if>>${cat.nome}</option>
                </c:forEach>
            </select>
            <button type="submit">Filtra</button>
        </form>
        <c:choose>
            <c:when test="${empty eventiDisponibili}">
                <c:choose>
                    <c:when test="${not empty param.categoriaId}">
                        <c:forEach var="cat" items="${categorie}">
                            <c:if test="${cat.id == param.categoriaId}">
                                <p>Nessun evento per la categoria: ${cat.nome}</p>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Non ci sono eventi disponibili al momento.</p>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:set var="categoriaFiltrata" value="${param.categoriaId}" />
                <div class="eventi-container">
                    <c:forEach var="evento" items="${eventiDisponibili}">
                        <c:if test="${empty categoriaFiltrata or categoriaFiltrata == '' or evento.categoria.id == categoriaFiltrata}">
                            <div class="evento-card">
                                <h3>${evento.nome}</h3>
                                <p><strong>Data:</strong> ${evento.data}</p>
                                <p><strong>Ora:</strong> ${evento.ora}</p>
                                <p><strong>Luogo:</strong> ${evento.luogo}</p>
                                <p><strong>Organizzatore:</strong> ${evento.organizzatore.username}</p>
                                <p><strong>Categoria:</strong> ${evento.categoria.nome}</p>
                                <p class="descrizione"><strong>Descrizione:</strong> ${evento.descrizione}</p>
                                <p><strong>Posti disponibili:</strong> ${evento.postiDisponibili}</p>
                                <form action="${pageContext.request.contextPath}/iscriviti" method="post">
                                    <input type="hidden" name="eventoId" value="${evento.id}">
                                    <button type="submit" class="btn-iscrizione">Iscriviti</button>
                                </form>
                                <button class="btn-calendar" data-evento-id="${evento.id}" data-is-registered="false" disabled title="Devi prima iscriverti all'evento">
                                    <i class="fas fa-calendar-plus"></i> Aggiungi a Google Calendar
                                </button>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<footer>
    <p>&copy; Tito Catalano, studente di Ingegneria Digitale</p>
</footer>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const calendarButtons = document.querySelectorAll('.btn-calendar');

        calendarButtons.forEach(button => {
            const isRegistered = button.getAttribute('data-is-registered') === 'true';

            if (isRegistered) {
                button.addEventListener('click', function() {
                    const eventoId = this.getAttribute('data-evento-id');
                    const originalContent = this.innerHTML;

                    // Show loading indicator
                    this.disabled = true;
                    this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Aggiunta in corso...';

                    // AJAX call
                    fetch('addToGoogleCalendar?id=' + eventoId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                // Mark as successful
                                this.innerHTML = '<i class="fas fa-check"></i> Aggiunto';

                                // Open the calendar link in a new tab
                                window.open(data.calendarLink, '_blank');
                            } else {
                                // Show error message
                                alert('Errore: ' + data.message);
                                this.disabled = false;
                                this.innerHTML = originalContent;
                            }
                        })
                        .catch(error => {
                            console.error('Errore:', error);
                            alert('Si è verificato un errore durante l\'aggiunta al calendario.');
                            this.disabled = false;
                            this.innerHTML = originalContent;
                        });
                });
            }
        });
    });
</script>
</body>
</html>