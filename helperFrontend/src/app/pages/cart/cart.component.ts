import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  totalPrice = 0;
  listOfProduct: Product[] = [];
  constructor(
    private notification: NzNotificationService,
    private modal: NzModalService,
    private router: Router
  ) {}
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }

  ngOnInit(): void {
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.listOfProduct = JSON.parse(cart);
      this.listOfProduct.forEach((ele) => {
        this.totalPrice += ele.quanlityBuy * ele.productPrice;
      });
    }
  }

  plusProduct(item) {
    this.listOfProduct.forEach((ele) => {
      if (ele.productID == item.productID &&  item.quanlityBuy < ele.productQuantily ) {
        ele.quanlityBuy += 1;
      }
    });
    this.updateCart();
  }

  minusProduct(item) {
    this.listOfProduct.forEach((ele) => {
      if (ele.productID == item.productID && ele.quanlityBuy > 1) {
        ele.quanlityBuy -= 1;
      }
    });
    this.updateCart();
  }

  delProduct(id) {
    this.listOfProduct = this.listOfProduct.filter(
      (ele) => ele.productID != id
    );
    this.updateCart();
  }

  updateCart() {
    this.totalPrice = 0;
    this.listOfProduct.forEach((ele) => {
      this.totalPrice += ele.quanlityBuy * ele.productPrice;
    });
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.listOfProduct));
  }

  payment() {
    const userAuth = localStorage.getItem('auth') || '';
    if (userAuth) {
      this.router.navigate(['/payment']);
      this.updateCart();
    } else {
      this.modal.confirm({
        nzTitle: 'Bạn cần đăng nhập để thanh toán!',
        nzContent: '',
        nzOkText: 'Đăng nhập',
        nzOkType: 'primary',
        nzOkDanger: false,
        nzOnOk: () => {
          this.router.navigate(['/login/1']);
          this.updateCart();
        },
        nzCancelText: 'Ở lại',
        nzOnCancel: () => console.log('Cancel'),
      });
    }
  }
}
