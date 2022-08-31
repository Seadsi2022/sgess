import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvariable } from '../evariable.model';

@Component({
  selector: 'jhi-evariable-detail',
  templateUrl: './evariable-detail.component.html',
})
export class EvariableDetailComponent implements OnInit {
  evariable: IEvariable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evariable }) => {
      this.evariable = evariable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
