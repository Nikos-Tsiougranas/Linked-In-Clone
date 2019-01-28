import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent{
  @ViewChild('f') loginform: NgForm;

  constructor(private login: LoginService, private router:Router){}

  onSubmit(){
    this.login.login(this.loginform.value.email,this.loginform.value.password).subscribe(
      res=>{
        console.log('res',res);
        if(res){
          if(this.loginform.value.email=="admin")
          {
            this.router.navigate(['admin']);
            return;
          } 
          this.router.navigate(['home']);
        }
        else{
          window.alert("Wrong email and/or password!");
        }
        
      },
      error => {
  			console.log(error);
  		}
    );
  }
}