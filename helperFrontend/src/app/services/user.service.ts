import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpBaseService) {}
  login(userName, passWord): Observable<any> {
    const user = { username: userName, password: passWord };
    return this.http.post<any>(`/api/v1/login`, user);
  }
  saveUser(user): Observable<any> {
    return this.http.post<any>(`/api/v1/user/`, user);
  }
  getUser(id): Observable<any> {
    return this.http.get<any>(`/api/v1/user/` + id, null);
  }
}
