import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import * as h from '../host'; 

@Injectable({
  providedIn: 'root'
})
export class JobsService {
  
  constructor(private http:HttpClient,private sessionSt:SessionStorageService) { }
  getjobs(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')),
      })
    };
    return this.http.get(h.host+'/getjobs',httpOptions);
  }
  postjob(jobname,description,skills){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')+":"+jobname+":"+description+":"+skills),
      })
    };
    return this.http.get(h.host+'/postjob',httpOptions);
  }
  applicatejob(jobid){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Basic ' + btoa(this.sessionSt.retrieve('email')+':'+this.sessionSt.retrieve('password')+":"+jobid),
      })
    };
    return this.http.get<boolean>(h.host+'/applicatejob',httpOptions);
  }
}
