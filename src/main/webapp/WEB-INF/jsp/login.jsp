
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h3>Login Form</h3>

<div class="container">

    <form method="post"  >

        <div class="form-group">
            <label>email :</label>
            <input type="email" name="email" />
        </div>
        <div class="form-group">
            <label>password :</label>
            <input type="password" name="password" />
        </div>

        <div class="form-group">
            <button type="submit" class="btn btn-primary">Enregistrer</button>
        </div>
    </form>
     <!--<p> Pas encore inscrit ? </p> <a class="btn btn-primary" href="/users/signup">S'inscrire</a> -->
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
