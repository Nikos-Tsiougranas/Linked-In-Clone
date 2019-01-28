import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AdminService } from '../services/admin.service';
import { User } from '../entities/user';
import { FormControl, FormBuilder } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-network',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  @ViewChild('f') search: NgForm;

  displaySwitch = true;
  users: User[] = [];
  downloadSwitch=false;
  name = 'Angular 6';
  categories = [
    {
      id: 1,
      name: 'CV'
    },
    {
      id: 2,
      name: 'Articles'
    },
    {
      id: 3,
      name: 'Jobs'
    },
    {
      id: 4,
      name: 'Professional Expertise'
    },
    {
      id: 5,
      name: 'Likes'
    },
    {
      id: 6,
      name: 'Comments'
    },
    {
      id: 7,
      name: 'Network'
    },
    
  ];

  categoriesSelected = [
    false, false, false,false, false, false,false
  ];

  myGroup;  
  
  constructor(private formBuilder: FormBuilder, private admin:AdminService, private sanitizer: DomSanitizer) {
    this.myGroup = this.formBuilder.group({
      myCategory: this.formBuilder.array(this.categoriesSelected)
    });
  }

  ngOnInit() {
    this.users=[];
    this.admin.getAllUsers().subscribe(
      (users: any[]) => {
        console.log(users);
        for(var i in users){
          if(users[i].firstname=="admin")
            continue;
          console.log(i);
          console.log(users[i]);
          console.log(users[i].firstname+ " " +users[i].lastname);
          this.users[i] = users[i];
        }
      }
    );
  }

  onSubmit(){
    this.users=[];
    console.log(this.search);
    console.log(this.search.value.search);
    this.admin.search(this.search.value.search).subscribe(
      (users: any[]) => {
        console.log(users);
        for(var i in users){
          if(users[i].firstname=="admin")
            continue;
          console.log(i);
          console.log(users[i]);
          console.log(users[i].firstname+ " " +users[i].lastname);
          this.users[i] = users[i];
        }
      }
    );
    this.displaySwitch = false;
  }

  Download()
  {
    console.log(this.myGroup.get('myCategory').value[3]);
    this.admin.getXML(this.myGroup.get('myCategory').value).subscribe(
      (res) => {
        console.log('start download:',res);
        var url = window.URL.createObjectURL(res);
        var a = document.createElement('a');
        document.body.appendChild(a);
        a.setAttribute('style', 'display: none');
        a.href = url;
        a.download = "myxml.xml";
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove(); // remove the element
      }
    );
  }

}