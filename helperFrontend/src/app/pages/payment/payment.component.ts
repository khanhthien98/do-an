import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Bill } from 'src/app/entity/Bill.model';
import { User } from 'src/app/entity/User.model';
import { BillService } from 'src/app/services/bill.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css'],
})
export class PaymentComponent implements OnInit {
  user: User = null;
  bill = new Bill();
  userName: string;
  phone: string;
  address: string;
  userId: string;
  payment: string;
  validateForm!: FormGroup;
  total: number;
  cart: string;
  constructor(
    private router: Router,
    private userService: UserService,
    private notification: NzNotificationService,
    private fb: FormBuilder,
    private billService: BillService
  ) {}

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    this.cart = localStorage.getItem('cart') || '';
    this.userId = localStorage.getItem('userId') || '';
    this.total = Number.parseInt(localStorage.getItem('total') || '');
    if (this.cart == '') {
      this.router.navigateByUrl('/home');
    }
    this.validateForm = this.fb.group({
      userName: [null, [Validators.required]],
      phone: [null, [Validators.required]],
      address: [null, [Validators.required]],
      payment: ['Live', [Validators.required]],
    });
    if (this.userId) {
      this.getUser(this.userId);
    }
  }
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      if (this.validateForm.controls.hasOwnProperty(i)) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
    this.userName = this.validateForm.controls.userName.value;
    this.phone = this.validateForm.controls.phone.value;
    this.address = this.validateForm.controls.address.value;
    this.payment = this.validateForm.controls.payment.value;

    if (this.userName && this.phone && this.address && this.payment) {
      this.bill.name = this.userName;
      this.bill.phone = this.phone;
      this.bill.address = this.address;
      this.bill.payment = this.payment;
      this.bill.total = this.total;
      this.bill.user = {
        userID: Number.parseInt(this.userId),
        userName: '',
        userEmail: '',
        userPhone: '',
        userPass: '',
      };
      this.bill.products = this.cart;
      this.saveBill(this.bill);
    }
  }

  getUser(userId) {
    this.userService.getUser(userId).subscribe(
      (data) => {
        if (data && data.result) {
          this.user = data.result;
          this.validateForm.controls['userName'].setValue(this.user.userName);
          this.validateForm.controls['phone'].setValue(this.user.userPhone);
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }

  saveBill(bill) {
    this.billService.saveBill(bill).subscribe(
      (data) => {
        if (data && data.result) {
          this.router.navigate(['/pay-success']);
          localStorage.removeItem('cart');
          localStorage.removeItem('total');
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
}
