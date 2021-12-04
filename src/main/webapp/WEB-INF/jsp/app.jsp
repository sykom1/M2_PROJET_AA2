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
                    <a class="nav-link" href="#" @click="viewUserByMail(mail)" v-if="token != null ">Moi</a>
                </li>

                <li class="nav-item active">
        <a class="nav-link" href="#" @click="goToLogin" v-if="token == null" >Login</a>
            </li>
                <li class="nav-item active">
        <a class="nav-link" href="#" v-on:click="logout()" v-if="token != null" >Logout</a>
            </li>

            </ul>

        </div>
        <form class="form-inline my-3 my-lg-0">
            <input class="form-control mr-sm-2" id="search" type="search" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" @click="search" type="submit">Search</button>
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



















    <div class="panel-body inf-content" v-if="currentUser != null">
    <div class="row">
        <div class="col-md-4">
            <img alt="" style="width:600px;" title="" class="img-circle img-thumbnail isTooltip" src="https://bootdey.com/img/Content/avatar/avatar7.png" data-original-title="Usuario">
        </div>

    <div class="col-md-6" >
        <strong>Informations</strong><br>
        <div class="table-responsive">
            <table class="table table-user-information">
                <tbody>
                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-asterisk text-primary"></span>
                            Numero d'identifitacion
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.id}}
                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-user  text-primary"></span>
                            Name
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.firstname}}
                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-cloud text-primary"></span>
                            Lastname
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.lastname}}
                    </td>
                </tr>

                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-bookmark text-primary"></span>
                            Email
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.email}}
                    </td>
                </tr>

                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-calendar text-primary"></span>
                            Website
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.website}}
                    </td>

                </tr>
                <tr>
                    <td>
                        <strong>
                            <span class="glyphicon glyphicon-calendar text-primary"></span>
                            Date de naissance
                        </strong>
                    </td>
                    <td class="text-primary">
                        {{currentUser.birthday}}
                    </td>
                </tr>

                </tbody>


            </table>
        </div>
    </div>
    </div>
        <div>
            <a  href="#" v-if="token == currentUser.token && token != null" @click="addActivity()" class="btn btn-primary">Ajouter une activite</a>
        </div>
    </div>


    <div class="panel-body inf-content" v-if="currentUser != null">
        <div class="col-md-6" >
            <h2><strong class="font-weight-bold">Curriculum vitae</strong></h2>
                <br>
            <div class="table-responsive">
                <table class="table table-user-information" v-for="activity in listCurrentActivities" >

                    <tbody >


                    <td><h4 v-if="token == currentUser.token && token != null"
                            @click.prevent="removeActivity(activity.id,currentUser.id)">
                        <i class="bi bi-trash hover-bg-black" ></i></h4></td>
                    <tr>
                        <td>
                            <strong>
                                <span class="glyphicon glyphicon-asterisk text-primary"></span>
                                Nature
                            </strong>
                        </td>

                        <td class="text-primary">
                            {{activity.nature}}

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <strong>
                                <span class="glyphicon glyphicon-user  text-primary"></span>
                                Title
                            </strong>
                        </td>
                        <td class="text-primary">
                            {{activity.title}}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <strong>
                                <span class="glyphicon glyphicon-cloud text-primary"></span>
                                Annee
                            </strong>
                        </td>
                        <td class="text-primary">
                            {{activity.year}}
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <strong>
                                <span class="glyphicon glyphicon-bookmark text-primary"></span>
                                Email
                            </strong>
                        </td>
                        <td class="text-primary">
                            {{currentUser.email}}
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <strong>
                                <span class="glyphicon glyphicon-calendar text-primary"></span>
                                Website
                            </strong>
                        </td>
                        <td class="text-primary">
                            {{activity.website}}
                        </td>

                    </tr>

                    </tbody>


                </table>

            </div>
        </div>
    </div>




























    <div v-if="currentActivity != null">



    </div>


        <div class="container" v-if="loginPage != null">

            <form class="vh-100 gradient-custom">
                <div class="container py-5 h-100">
                    <div class="row d-flex justify-content-center align-items-center h-100">
                        <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                            <div class="card bg-dark text-white" style="border-radius: 1rem;">
                                <div class="card-body p-5 text-center">

                                    <div class="mb-md-5 mt-md-4 pb-5">

                                        <h2 class="fw-bold mb-2 text-uppercase">Login</h2>
                                        <p class="text-white-50 mb-5">Veuillez entrer vos identifiants!</p>

                                        <div class="form-outline form-white mb-4">
                                            <label class="form-label" for="typeEmailX">Email</label>
                                            <input id="email" type="email" id="typeEmailX" class="form-control form-control-lg" />

                                        </div>

                                        <div class="form-outline form-white mb-4">
                                            <label class="form-label" for="typePasswordX">Password</label>
                                            <input id="password" type="password" id="typePasswordX" class="form-control form-control-lg" />

                                        </div>

                                        <button @click="login" class="btn btn-outline-light btn-lg px-5" type="submit">Sign in</button>
                                        <div v-if="(errors.login)" class="alert alert-warning">
                                            {{errors.login}}
                                        </div>

                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

        </div>


















<div class="card">
    <form  class="well form-horizontal" method="post" novalidate="true" v-if="editable != null">


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
                <select  v-model="activity.nature" class="form-control">
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
</div>











    <form  method="post" novalidate="true" v-if="editableCv != null">
        <div class="form-group" >
            <label>Titre :</label>
            <input v-model="editableCv.title" class="form-control" required="required" />
        </div>
        <div v-if="(errors.title)" class="alert alert-warning">
            {{errors.title}}
        </div>
        <div class="form-group">
            <label>Annee :</label>
            <input required="required" type="number" v-model="editableCv.year" class="form-control"/>
        </div>
        <div v-if="(errors.year)" class="alert alert-warning">
            {{errors.year}}
        </div>
        <div class="form-group">
            <label>Description :</label>
            <input  type="text" v-model="editableCv.desc" class="form-control"/>
        </div>
        <div class="form-group">
            <label>Nature :</label>
            <select  v-model="editableCv.nature" class="form-control" required="required">
            <option selected="selected" value="PROFESSIONAL_EXPERIENCES">PROFESSIONAL_EXPERIENCES</option>
            <option value="PROJECTS">PROJECTS</option>
            <option value="FORMATIONS">FORMATIONS</option>
            </select>
        </div>
        <div v-if="(errors.nature)" class="alert alert-warning">
            {{errors.nature}}
        </div>
        <div class="form-group">
            <label>Website :</label>
            <input  type="text" v-model="editableCv.website" class="form-control"/>
        </div>

        <div>
            <button v-on:click.prevent="submitActivity" class="btn btn-success">
                Enregistrer</button>
            <button v-on:click="refresh()" class="btn btn-danger">
                Abandonner</button>
        </div>

    </form>


</div>
<script src="${app}"></script>



<%@ include file="/WEB-INF/jsp/footer.jsp"%>