import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contact } from '../entity/Conntact.model';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  constructor(private http: HttpBaseService) {}
  saveContact(contact:Contact): Observable<any> {
    return this.http.post<any>(`/api/v1/contact`, contact);
  }
}
