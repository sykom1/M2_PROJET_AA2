const myApp = {

    // Préparation des données
    data() {
        console.log("data");
        return {
            counter: 1,
            message: "Hello",
            list: [10, 20, 30],
            axios: null,
            listUsers : [],
            currentUser : null,
            editable : null,
            errors: [],
            added :null
        }
    },

    // Mise en place de l'application
    mounted() {

        console.log("Mounted ");
        this.axios = axios.create({
            baseURL: 'http://localhost:8081/',
            timeout: 1000,
            headers: { 'Content-Type': 'application/json' },
        });
        this.refresh();
    },

    methods: {
        // Place pour les futures méthodes
        incCounter: function(step) {

            console.log("incremente le compteur ");
            this.counter+=step;
            this.axios.get('/users/1')
                .then(r => {
                    console.log("read user 1 done");
                    this.message = r.data;
                });
        },
        deleteUser: function (id){
            this.axios.delete('/users/' + id).then(this.refresh);
        },
        editUser: function (id){
            this.axios.get("/users/"+id).then(r => this.editable = r.data);
        },
        viewUser: function (id){
            console.log("salut")
            this.axios.get('/users/' + id).then(r =>{
                this.currentUser = r.data;

            })
        },
        refresh: function (){
            this.axios.get("/users").then(r => {
                this.listUsers = r.data;
            });
        },
        populateUsers: function (){

            for(let i = this.listUsers.length; i < this.listUsers.length+3;i++) {
                const user = {firstname: "user" + i, lastname: 1999 + i, email: "tu connais " + i}
                this.axios.post("/users", user).then(() => this.refresh());

            }
        },
        submitUser: function (id){
            if(this.editable.firstname === ""){
                this.errors.firstname= "c'est vide";
            }else if(this.editable.lastname === ""){
                this.errors.lastname = "vide";
            }
            else {
                if (this.added != null) {
                    console.log(this.editable);
                    console.log(this.added);
                    this.axios.post("/users", this.editable).then(() => {
                        this.refresh();
                        this.editable = null;
                        this.added = null;
                    });
                } else {
                    this.axios.put("/users/" + id, this.editable).then(() => {
                        this.refresh();
                        this.editable = null;
                        this.errors.firstname = "";
                        this.errors.lastname = "";
                        this.errors.birthday = "";
                        this.errors.password = "";
                        this.errors.email = "";
                        this.errors.website = "";
                    });
                }
            }
        },
        addUser : function (){
            this.editable = {email : "",password: "",firstname: "", lastname: "", website: "",birthday: null}
            this.added = true;
        },
        subUser: function (name,year,desc){

        }

    },



}

Vue.createApp(myApp).mount('#myApp');
Vue.createApp(myApp).mount('#app');