const myApp = {

    // Préparation des données
    data() {
        return {
            axios: null,
            listUsers : [],
            currentUser : null,
            currentActivity : null,
            editable : null,
            editableCv : null,
            errors: [],
            added :null,
            token : null,
            listActivities : [],
            listCurrentActivities : [],
            swap : false,
            addedCv : null,
            idPage : 0,
            nbPage : 0,
            searcher : null,
            loginPage : null,
            mail: null,

        }
    },

    // Mise en place de l'application
    mounted() {




        this.token = this.getCookie('access_token');



        if (this.token != null && this.token !== "") {
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',

                headers: {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + this.token},

            });
        } else {
            this.token = null;
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',
                headers: {'Content-Type': 'application/json'}
            });
        }

        this.getAll();





    },

    methods: {
        // Place pour les futures méthodes

        editUser: function (id){
            this.currentUser = null;
            this.axios.get("/users/"+id).then(r => {
                this.editable = r.data;
            });
        },
        range : function(start, end){
            if (!end) {
                end = start
                start = 0
            }
            start -= 1
            end -= 1
            let arr = []
            while (start++ !== end) {
                if(start > 1 && start < this.nbPage ){
                    arr.push(start)
                }

            }
            return arr;
        },
        viewUser: function (id){

            this.swap = true;
            this.axios.get('/users/' + id).then(r =>{
                this.currentUser = r.data;
                this.listCurrentActivities = this.currentUser.cv;

            })
        },
        viewUserByMail : function (email){
            this.swap = true;
            this.axios.get('/users/mail/' + email.toLowerCase()).then(r =>{
                this.currentUser = r.data;
                this.listCurrentActivities = this.currentUser.cv;

            })
        },
        getAll: function (){
            this.axios.get("/users").then(r => {
                let onelist = r.data;
                let value = ((onelist.length)/20);
                if(Number.isInteger(value)) {
                    this.nbPage = value;
                }else{
                    this.nbPage = Math.floor(value) + 1;
                }
            }).then(()=> {
                this.refresh()
            })
        },
        viewActivity : function (id){
            this.currentUser = null;
            this.axios.get('/activities/' + id).then(r =>{
                this.currentActivity = r.data;
            })
        },
        refresh: function (){

            this.searcher = null;

            this.axios.get("/users/page/" + this.idPage).then(r => {


                this.listUsers = r.data;

            });

            this.axios.get("/activities").then(r => {
                this.listActivities = r.data;
            });

        }
        ,
        validateEmail(email) {
            const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(email);
        },
        isValidHttpUrl(string) {
            let url;

            try {
                url = new URL(string);
            } catch (_) {
                return false;
            }

            return url.protocol === "http:" || url.protocol === "https:";
        },
        checkPassword(str)
        {
            // at least one number, one lowercase and one uppercase letter
            // at least six characters that are letters, numbers or the underscore
            var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{8,}$/;
            return re.test(str);
        },
        submitUser: function (id){

            const AuthStr = 'Bearer '.concat(this.token);

            this.editable.password = this.editable.pass;
            this.editable.pass = null;
            if(this.editable.firstname === ""){
                this.errors.firstname= "écrire son prenom";
            }if(this.editable.lastname === ""){
                this.errors.lastname = "rempir votre nom de famille";
            }
            if(!this.validateEmail(this.editable.email)){
                this.errors.email = "email non valide";
            }
            if(this.editable.birthday === null){
                this.errors.birthday = "remplir la date de naissance";
            }
            if(!this.isValidHttpUrl(this.editable.website)){
                this.errors.website = "Veuillez entrer une url valable";
            }

            else {
                if (this.added != null) {

                    this.axios.post("/users/signup", this.editable,{params:{
                            email: this.editable.email, password: this.editable.password
                        }, headers: { Authorization: AuthStr }})
                        .then(() =>{
                            this.editable.token = null;
                            this.resetAll()
                            this.swapActUsers()
                            this.getAll()
                            this.refresh()
                    })

                } else {


                        this.axios.put("/users/" + id, this.editable, {headers: { Authorization: AuthStr }}).then(() => {

                        }).then( ()=>{
                            this.axios.get("/users/" + this.editable.id).then(r => {
                                this.editable.cv.forEach(element => {
                                    element.user = r.data;
                                    this.axios.put("/activities/" + element.id, element,{headers: { Authorization: AuthStr }})
                                        .then(() =>{
                                            this.viewUser(this.editable.id)
                                            this.editable = null;
                                            this.errors.firstname = "";
                                            this.errors.lastname = "";
                                            this.errors.birthday = "";
                                            this.errors.password = "";
                                            this.errors.email = "";
                                            this.errors.website = "";

                                        })

                                })});
                        } );
                }
            }
        },

        addUser : function (){
            this.resetAll()
            this.swap = true;
            this.currentUser = null;
            this.editable = {email : "",password: "",firstname: "", lastname: "", website: "",birthday: null,token: null,cv:null}
            this.added = true;

        },submitActivity : function (e){

            const AuthStr = 'Bearer '.concat(this.token);
            this.editableCv.user = this.currentUser;
            if(this.editableCv.nature ==null){
                this.errors.nature = "Veuillez entrer une nature"
            }
            if(this.editableCv.title == null ||this.editableCv.title === ""){
                this.errors.title = "Veuillez ajouter un titre";
            }
            if(this.editableCv.year <= 1980 || this.editableCv.year > 2021){
                this.errors.year = "Veuillez entrer une date valide";
            }
            else {
            this.axios.post("/activities", this.editableCv)
                .then(() =>{

                    this.editableCv = null;
                    this.addedCv = null;
                    this.errors = []
                }).then(() => {
                this.viewUser(this.currentUser.id)

            });
            }
        e.preventDefault()


        },
        modifyIdPage : function (id){
            this.idPage = id-1;
            this.refresh();
        }

        ,
        addActivity : function (){

            this.editableCv = {title : "",year: 0,nature: null, desc: "", website: "",xUser: null}
            this.addedCv = true;

        },
        goToLogin : function () {
            this.resetAll()
            this.swap = true;
            this.currentUser = null;
            this.loginPage = true;

        }
        ,
        login : function(e){

            let email = document.getElementById('email').value;
            let password = document.getElementById('password').value;
            this.axios.post("/users/signin",null, {params: {
                            email: email, password: password}})
                    .then(r=>{
                    this.errors.login = null;
                    let tok = r.data;
                    this.token = tok;
                    let cookievalue,
                        cookieexpire,
                        cookiepath,
                        date;

                    cookievalue =tok;
                    date = new Date();
                    date.setTime(date.getTime() + 3600000); // will last 3600 seconds (1 hour)
                    cookieexpire = date.toGMTString();

                    cookiepath = "/"; // accessible from every web page of the domain

                    if(tok != null){
                        document.cookie="access_token=" + cookievalue + "; expires=" + cookieexpire + "; path=" + cookiepath;

                    }else{
                        document.cookie="access_token=" + ""
                    }

                    this.loginPage = null;
                    this.mail= email;
                    this.resetAll();
                    this.swapActUsers();
                }).catch((e) => {
                        this.errors.login = "Mot de passe ou identifiant incorrect";
                });

            e.preventDefault()


        },
        logout: function (){

            const AuthStr = 'Bearer '.concat(this.token);
            axios.get("/users/logout", { headers: { Authorization: AuthStr } })
                .then(() => {
                    // If request is good...
                    this.token=null;
                    document.cookie="access_token=" + "";
                    this.added = null;
                    this.currentUser = null;
                    this.editable = null;
                    this.swapActUsers();
                    this.resetAll()
                    this.refresh()
                })
                .catch(() => {
                    this.token= null;
                    document.cookie="access_token=" + "";
                    this.added = null;
                    this.currentUser = null;
                    this.editable = null;
                    this.swapActUsers();
                    this.resetAll()
                    this.refresh()
                });
        },
        removeActivity: function (id,idU){

            let r = confirm("Voulez vous vraiment supprimer cette activité ?!");
            if (r === true) {
                this.axios.delete("/activities/"+id).then(() => {
                    this.viewUser(idU)
                });
            } else {

            }

        }
        ,
         getCookie : function (cName){
             const name = cName + "=";
             const cDecoded = decodeURIComponent(document.cookie); //to be careful
             const cArr = cDecoded.split('; ');

             let res = null;
             cArr.forEach(val => {
                 if (val.indexOf(name) === 0) res = val.substring(name.length);
             })
             return res
        },
        swapActUsers : function (){
            this.swap = false;
            this.added = null;
            this.editable = null;
            this.currentUser = null;
            this.currentActivity = null;
            this.loginPage = null;
            this.editableCv = null
            this.refresh();

        },
        resetAll : function (){
            this.swap = true;
            this.added = null;
            this.editable = null;
            this.currentUser = null;
            this.currentActivity = null;
            this.refresh();



        },
        search : function (e){
            let name = document.getElementById('search').value;
            this.axios.get("/users/search?name=" + name).then(r=> this.listUsers = r.data);

            this.searcher = true;
            this.swapActUsers();
            e.preventDefault()
        }
    }



}

Vue.createApp(myApp).mount('#myApp');