import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http } from "@angular/http";
import { map } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class PointService {

  constructor(private aHttp : Http, private http : HttpClient) { }

  private userUrl = 'http://localhost:8080/api/v1/points';
  private delUrl = 'http://localhost:8080/api/v1/points?id=';

  public getPoints() {
    return this.aHttp.get(this.userUrl).pipe(map(response => {
        {
          return response.json(); 
        };
    }));
  }

   public deleteUser(id) {
    return this.aHttp.delete(this.delUrl+id).pipe(map(response => {
        {
          return response.json(); 
        };
    }));
  }
  
}
