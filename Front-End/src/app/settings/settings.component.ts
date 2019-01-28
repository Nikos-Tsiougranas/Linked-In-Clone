import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { SettingsService } from '../services/settings.service';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent{
  @ViewChild('f') updateform: NgForm;
  constructor(private update:SettingsService,private sessionSt:SessionStorageService) { }

  onSubmit(){
    console.log(this.updateform);
    this.update.updateCredentials(this.updateform.value.email,this.updateform.value.password).subscribe(
      (res)=>{
        if(res == false){
          window.alert("Invalid credentials");
        }
        else{
          this.sessionSt.store('email',this.updateform.value.email);
          this.sessionSt.store('password',this.updateform.value.password);
        }
      }
    );
  }

}
