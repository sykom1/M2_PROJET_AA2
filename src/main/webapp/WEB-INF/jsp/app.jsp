<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:url var="home" value="/home" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="app" value="/app.js" />
<div id="myApp">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <!--<a class="navbar-brand" href="#" v-on:click="refresh()">List of users</a> -->
        <a class="navbar-brand" href="#" @click="addUser()" v-if="token != null ">Ajouter une personne</a>
        <a class="navbar-brand" href="#" @click="swapActUsers()" v-if="swap == true">Voir la liste des personnes</a>
        <a class="navbar-brand" href="/users/signin" v-if="token == null " >Login</a>
        <a class="navbar-brand" href="#" v-on:click="logout()" v-if="token != null " >Logout</a>

    </nav>


    <!--
    <div class="container">
        <h1>My application</h1>
        <p>{{ message }}</p>
        <p>counter = {{counter}}</p>
        <p>list = <span v-for="element in list">{{element}} - </span></p>
        <button v-on:click="incCounter(1)">Plus un</button>
        <button v-on:click="incCounter(2)">Plus deux</button>
        <span v-on:mouseover="incCounter(1)">Il faut me survoler</span>
    </div> -->

    <div class="container" v-if="editable == null && swap == false">
        <h1>Liste des Users</h1>
        <table class="table">
            <tr>
                <th>Nom</th>
                <th>Prenom</th>

            </tr>
            <tr v-for="user in listUsers">

                <td>{{user.firstname}}</td>
                <td>{{user.lastname}}</td>


                <td><a class="btn btn-primary btn-sm" @click="viewUser(user.id)" >Montrer</a></td>
                <td><a class="btn btn-primary btn-sm" @click="editUser(user.id)" v-if="token == user.token && token != null">Editer</a></td>


                <td><a class="btn btn-danger btn-sm" @click="deleteUser(user.id)" v-if="token == user.token && token != null">Supprimer</a></td>
            </tr>
        </table>

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

    </div>
    <div v-if="currentActivity != null">
        <h3>{{currentActivity.nature}}</h3>
        <h2>{{currentActivity.title}}</h2>
        <ul>
            <li>{{currentActivity.year}}</li>
            <li>{{currentActivity.desc}}</li>
        </ul>

    </div>



    <form id="app" method="post" novalidate="true" v-if="editable != null">

        <div class="form-group">
            <label>Firstname :</label>
            <input v-model="editable.firstname" class="form-control"
                   v-bind:class="{'is-invalid':errors.firstname}" />
            <div v-if="(errors.firstname)" class="alert alert-warning">
                {{errors.firstname}}
            </div>
        </div>
        <div class="form-group">
            <label>Lastname :</label>
            <input  v-model="editable.lastname" class="form-control"
                   v-bind:class="{'is-invalid':errors.lastname}"  />
            <div v-if="(errors.lastname)" class="alert alert-warning">
                {{errors.lastname}}
            </div>
        </div>
        <div class="form-group">
            <label>Email :</label>
            <input type="email" v-model="editable.email" class="form-control"
                   v-bind:class="{'is-invalid':errors.email}"  />
            <div v-if="(errors.email)" class="alert alert-warning">
                {{errors.email}}
            </div>
        </div>

        <div class="form-group" v-if="added != null">
            <label>Password :</label>
            <input type="password" v-model="editable.password" class="form-control"
                   v-bind:class="{'is-invalid':errors.password}"  />
            <div v-if="(errors.password)" class="alert alert-warning">
                {{errors.password}}
            </div>
        </div>

        <div class="form-group" v-if="added != null">
            <label>CV :</label>
            <input type="text" v-model="editable.cv" class="form-control" />

        </div>

        <div class="form-group">
            <label>Website :</label>
            <input type="url" v-model="editable.website" class="form-control"
                   v-bind:class="{'is-invalid':errors.website}"  />
            <div v-if="(errors.website)" class="alert alert-warning">
                {{errors.website}}
            </div>
        </div>
        <div class="form-group">
            <label>Birthday :</label>
            <input type="date" placeholder="dd-mm-yyyy" v-model="editable.birthday" class="form-control"
                   v-bind:class="{'is-invalid':errors.birthday}"  />
            <div v-if="(errors.birthday)" class="alert alert-warning">
                {{errors.birthday}}
            </div>
        </div>
        <div class="form-group">
            <button v-on:click.prevent="submitUser(editable.id)" class="btn btn-primary">
                Save</button>
            <button v-on:click="refresh()" class="btn btn-danger">
                Abort</button>
        </div>
        <!--<button class="btn btn-dark" > Ajouter une activité</button>

        <div class="form-group">
            <label> Titre : </label>
            <input type="text" v-model=""/>
        </div> -->
    </form>

</div>
<script src="${app}"></script>



<%@ include file="/WEB-INF/jsp/footer.jsp"%>