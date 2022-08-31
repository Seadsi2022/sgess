import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEattributvariable } from '../eattributvariable.model';

@Component({
  selector: 'jhi-eattributvariable-detail',
  templateUrl: './eattributvariable-detail.component.html',
})
export class EattributvariableDetailComponent implements OnInit {
  eattributvariable: IEattributvariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eattributvariable }) => {
      this.eattributvariable = eattributvariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
