const myApp = {

    // Préparation des données
    data() {
        console.log("data");
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
            paginateArray : [],
            idPage : 0,
            nbPage : 0,



        }
    },

    // Mise en place de l'application
    mounted() {
        this.token = this.getCookie('access_token');
        //this.token = localStorage.getItem("token");
        console.log("Mounted ");
        if(this.token != null && this.token !== ""){
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',
                timeout: 1000,
                headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' +  this.token},

            });
        }else {
            this.token = null;
            this.axios = axios.create({
                baseURL: 'http://localhost:8081/',
                timeout: 1000,
                headers: { 'Content-Type': 'application/json'}
            });
        }


        this.refresh();


    },

    methods: {
        // Place pour les futures méthodes



        editUser: function (id){
            this.axios.get("/users/"+id).then(r => {
                this.editable = r.data;
            });
        },
        viewUser: function (id){

            this.swap = true;
            this.axios.get('/users/' + id).then(r =>{
                this.currentUser = r.data;
                this.listCurrentActivities = this.currentUser.cv;
            })
        },
        viewActivity : function (id){
            this.currentUser = null;
            this.axios.get('/activities/' + id).then(r =>{
                this.currentActivity = r.data;
            })
        },
        refresh: function (){

            this.axios.get("/users").then(r => {
                let onelist = r.data;
                this.nbPage = Math.floor(((onelist.length)/10)) + 1;

            }).then(() =>  this.axios.get("/users/page/" + this.idPage).then(r => {
                this.listUsers = r.data;
            }) );

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
            if(!this.checkPassword(this.editable.password)){
                this.errors.password = "Le mot de passe doit etre supérieur à 8 caracteres et doitcontenir au moins un chiffre,une majuscule, une majuscule ";
            }
            if(!this.isValidHttpUrl(this.editable.website)){
                this.errors.website = "Veuillez entrer une url valable";
            }

            else {
                if (this.added != null) {

                    this.axios.post("/users/signup", this.editable,{params:{
                            email: this.editable.email, password: this.editable.password
                        }, headers: { Authorization: AuthStr }},)
                        .then(() =>{
                            this.editable.token = null;
                            this.resetAll()
                            this.swapActUsers()
                    });

                } else {


                        this.axios.put("/users/" + id, this.editable, {headers: { Authorization: AuthStr }}).then(() => {

                        }).then( ()=>{
                            this.axios.get("/users/" + this.editable.id).then(r => {
                                this.editable.cv.forEach(element => {
                                    element.user = r.data;
                                    this.axios.put("/activities/" + element.id, element,{headers: { Authorization: AuthStr }})
                                        .then(() =>{
                                            this.refresh();
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

        },submitActivity : function (){

            const AuthStr = 'Bearer '.concat(this.token);
            this.editableCv.user = this.currentUser;
            this.axios.post("/activities", this.editableCv)
                .then(() =>{

                    this.editableCv = null;
                    this.addedCv = null;
                }).then(() => {
                this.viewUser(this.currentUser.id)

            });



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
        logout: function (){

            const AuthStr = 'Bearer '.concat(this.token);
            axios.get("/users/logout", { headers: { Authorization: AuthStr } })
                .then(response => {
                    // If request is good...
                    this.token=null;
                    document.cookie="access_token=" + "";
                    this.added = null;
                    this.currentUser = null;
                    this.editable = null;
                    this.refresh()
                })
                .catch((error) => {
                    this.token= null;
                    document.cookie="access_token=" + "";
                    this.added = null;
                    this.currentUser = null;
                    this.editable = null;
                    this.refresh()
                });
        },
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
        search : function (){
            let name = document.getElementById('search').value;
            this.axios.get("/users/search?name=" + name).then(r=> this.listUsers = r.data);
        }
    }



}

Vue.createApp(myApp).mount('#myApp');