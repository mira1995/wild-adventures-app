import {Component, OnInit} from '@angular/core';
import {HttpService} from "../services/http-service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public categories: Array<any>;

  constructor(private httpService: HttpService) {}

  ngOnInit() {
    this.categories = [];
    this.httpService.getCategories().subscribe((categories: Array<any>) => this.categories = categories);
  }
}
