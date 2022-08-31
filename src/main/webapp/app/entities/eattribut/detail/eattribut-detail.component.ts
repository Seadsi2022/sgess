import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEattribut } from '../eattribut.model';

@Component({
  selector: 'jhi-eattribut-detail',
  templateUrl: './eattribut-detail.component.html',
})
export class EattributDetailComponent implements OnInit {
  eattribut: IEattribut | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eattribut }) => {
      this.eattribut = eattribut;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
