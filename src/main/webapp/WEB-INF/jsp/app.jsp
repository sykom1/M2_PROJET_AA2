<%@ include file="/WEB-INF/jsp/header.jsp"%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="app" value="/app.js" />
<div id="myApp">

    <nav class="navbar navbar-expand-lg navbar navbar-dark bg-dark">
        <a class="navbar-brand" href="#" @click="swapActUsers()" >CV-GEST</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item active">
        <a class="nav-link" href="#" @click="addUser()" v-if="token != null ">Ajouter une personne</a>
                </li>

                <li class="nav-item active">
        <a class="nav-link" href="#" @click="goToLogin" v-if="token == null " >Login</a>
            </li>
                <li class="nav-item active">
        <a class="nav-link" href="#" v-on:click="logout()" v-if="token != null " >Logout</a>
            </li>

            </ul>

        </div>
        <form class="form-inline my-3 my-lg-0">
            <input class="form-control mr-sm-2" id="search" type="search" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" @click="search()" type="submit">Search</button>
        </form>
    </nav>




    <div class="container" v-if="editable == null && swap == false">



        <h1 class="text-center">Liste des Users</h1>
        <table class="table table-hover ">
            <thead>
            <tr>
                <th>Nom</th>
                <th>Prenom</th>

            </tr>
            </thead>
            <tbody>
            <tr v-for="user in listUsers">

                <td>{{user.firstname}}</td>
                <td>{{user.lastname}}</td>


                <td><a class="btn btn-dark btn-sm" @click="viewUser(user.id)" >Montrer</a></td>
                <td><a class=" btn btn-info" @click="editUser(user.id)" v-if="token == user.token && token != null">Editer</a></td>
                <!--<td><a class="btn btn-danger btn-sm" @click="deleteUser(user.id)" v-if="token == user.token && token != null">Supprimer</a></td>
                -->

            </tr>
            </tbody>
        </table>

        <nav v-if="searcher == null" aria-label="Page navigation example ">
            <ul class="pagination justify-content-center" >
                <li  class="page-item"> <a class="page-link" @click="modifyIdPage(1)" href="#">1</a></li>
                <li  class="page-item " v-for="id in range(idPage-5,idPage+5)"> <a class="page-link" @click="modifyIdPage(id)" href="#">{{id}}</a></li>
                <li  class="page-item"> <a class="page-link" @click="modifyIdPage(nbPage)" href="#">{{nbPage}}</a></li>
            </ul>
        </nav>
    </div>
    <div v-if="currentUser != null">
        <h3>{{currentUser.firstname}} {{currentUser.lastname}} </h3>
        <h4>{{currentUser.email}}</h4>
        <h4>{{currentUser.website}}</h4>
        <h4>{{currentUser.birthday}}</h4>
        <h4>CV : </h4>
        <div v-for="activity in listCurrentActivities">
            <h3><a href="#" @click="viewActivity(activity.id)">{{activity.title}}</a></h3>
        </div>

        <div>
            <a  href="#" v-if="token == currentUser.token && token != null" @click="addActivity(currentUser.id)" class="btn btn-primary">Ajouter une activite</a>
        </div>

    </div>
    <div v-if="currentActivity != null">
        <h3>{{currentActivity.nature}}</h3>
        <h2>{{currentActivity.title}}</h2>
        <ul>
            <li>{{currentActivity.year}}</li>
            <li>{{currentActivity.desc}}</li>
        </ul>

    </div>


        <div class="container" v-if="loginPage != null">

            <h2>Login</h2>
            <form novalidate="true">

                <div class="form-group">
                    <label>email :</label>
                    <input id="email" type="email"  />
                </div>
                <div class="form-group">
                    <label>password :</label>
                    <input id="password" type="password"  />
                </div>

                <div class="form-group">
                    <button type="submit" @click="login" class="btn btn-primary">Enregistrer</button>
                </div>
                <div v-if="(errors.login)" class="alert alert-warning">
                    {{errors.login}}
                </div>
            </form>
        </div>




    <form  method="post" novalidate="true" v-if="editable != null">


        <div class="form-row">
            <div class="form-group col-md-6">
            <label>Firstname :</label>
            <input v-model="editable.firstname" class="form-control"
                   v-bind:class="{'is-invalid':errors.firstname}" />
            <div v-if="(errors.firstname)" class="alert alert-warning">
                {{errors.firstname}}
            </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-md-6">
            <label>Lastname :</label>
            <input  v-model="editable.lastname" class="form-control"
                   v-bind:class="{'is-invalid':errors.lastname}"  />
            <div v-if="(errors.lastname)" class="alert alert-warning">
                {{errors.lastname}}
            </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-4">
                <label for="inputEmail4">Email</label>
                <input  placeholder="Email" id="inputEmail4" type="email" v-model="editable.email" class="form-control"
                       v-bind:class="{'is-invalid':errors.email}" >
                <div v-if="(errors.email)" class="alert alert-warning">
                    {{errors.email}}
                </div>
            </div>
            <div class="form-group col-md-4">
                <label>Password :</label>
                <input type="password" v-model="editable.password" class="form-control"
                       v-bind:class="{'is-invalid':errors.password}"  />
                <div v-if="(errors.password)" class="alert alert-warning">
                    {{errors.password}}
                </div>
            </div>
        </div>


        <div class="form-row">
            <div class="form-group col-md-4">
            <label>Website :</label>
            <input type="url" v-model="editable.website" class="form-control"
                   v-bind:class="{'is-invalid':errors.website}"  />
            <div v-if="(errors.website)" class="alert alert-warning">
                {{errors.website}}
            </div>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
            <label>Birthday :</label>
            <input type="date" placeholder="dd-mm-yyyy" v-model="editable.birthday" class="form-control"
                   v-bind:class="{'is-invalid':errors.birthday}"  />
            <div v-if="(errors.birthday)" class="alert alert-warning">
                {{errors.birthday}}
            </div>
            </div>
        </div>


        <div @if="added == null" v-for="activity in editable.cv">
            <h2>CV</h2>
            <div class="form-group" >
                <label>Titre :</label>
                <input v-model="activity.title" class="form-control"/>
            </div>

            <div class="form-group">
                <label>Annee :</label>
                <input  type="number" v-model="activity.year" class="form-control"/>
            </div>
            <div class="form-group">
                <label>Description :</label>
                <input  type="text" v-model="activity.desc" class="form-control"/>
            </div>
            <div class="form-group">
                <label>Nature :</label>
                <select v-model="activity.nature" class="form-control">
                    <option value="PROFESSIONAL_EXPERIENCES">PROFESSIONAL_EXPERIENCES</option>
                    <option value="PROJECTS">PROJECTS</option>
                    <option value="FORMATIONS">FORMATIONS</option>
                </select>
            </div>
            <div class="form-group">
                <label>Website :</label>
                <input  type="text" v-model="activity.website" class="form-control"/>
            </div>
        </div>



        <div class="form-group">
            <button v-on:click.prevent="submitUser(editable.id)" type="button" class="btn btn-success">Enregistrer</button>

            <button v-on:click="refresh()" class="btn btn-danger">Annuler</button>

        </div>

    </form>


    <form  method="post" novalidate="true" v-if="editableCv != null">
        <div class="form-group" >
            <label>Titre :</label>
            <input v-model="editableCv.title" class="form-control"/>
        </div>

        <div class="form-group">
            <label>Annee :</label>
            <input  type="number" v-model="editableCv.year" class="form-control"/>
        </div>
        <div class="form-group">
            <label>Description :</label>
            <input  type="text" v-model="editableCv.desc" class="form-control"/>
        </div>
        <div class="form-group">
            <label>Nature :</label>
            <select v-model="editableCv.nature" class="form-control">
            <option value="PROFESSIONAL_EXPERIENCES">PROFESSIONAL_EXPERIENCES</option>
            <option value="PROJECTS">PROJECTS</option>
            <option value="FORMATIONS">FORMATIONS</option>
            </select>

        </div>
        <div class="form-group">
            <label>Website :</label>
            <input  type="text" v-model="editableCv.website" class="form-control"/>
        </div>
        <div>
            <button v-on:click.prevent="submitActivity()" class="btn btn-success">
                Enregistrer</button>
            <button v-on:click="refresh()" class="btn btn-danger">
                Abandonner</button>
        </div>

    </form>

</div>
<script src="${app}"></script>



<%@ include file="/WEB-INF/jsp/footer.jsp"%>