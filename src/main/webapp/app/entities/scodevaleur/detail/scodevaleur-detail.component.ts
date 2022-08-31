import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IScodevaleur } from '../scodevaleur.model';

@Component({
  selector: 'jhi-scodevaleur-detail',
  templateUrl: './scodevaleur-detail.component.html',
})
export class ScodevaleurDetailComponent implements OnInit {
  scodevaleur: IScodevaleur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scodevaleur }) => {
      this.scodevaleur = scodevaleur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
