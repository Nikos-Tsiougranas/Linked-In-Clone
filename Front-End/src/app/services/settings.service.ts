import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }

  updateCredentials(email,password){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      }),
    };
    return this.http.put<boolean>(h.host+'/settings',email+':'+password,httpOptions);
  }
}
