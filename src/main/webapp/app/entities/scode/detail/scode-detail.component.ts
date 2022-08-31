import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScode } from '../scode.model';

@Component({
  selector: 'jhi-scode-detail',
  templateUrl: './scode-detail.component.html',
})
export class ScodeDetailComponent implements OnInit {
  scode: IScode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scode }) => {
      this.scode = scode;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
