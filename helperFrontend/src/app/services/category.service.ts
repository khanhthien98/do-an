import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpBaseService) {}
  getCategories(params: any): Observable<any> {
    return this.http.get<any>(`/api/v1/categories`, params);
  }
  getAllCategory(): Observable<any> {
    return this.http.get<any>(`/api/v1/categories/all`, null);
  }
}
