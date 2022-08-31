import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEattributvariable } from '../eattributvariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../eattributvariable.test-samples';

import { EattributvariableService } from './eattributvariable.service';

const requireRestSample: IEattributvariable = {
  ...sampleWithRequiredData,
};

describe('Eattributvariable Service', () => {
  let service: EattributvariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEattributvariable | IEattributvariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EattributvariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Eattributvariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eattributvariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eattributvariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Eattributvariable', () => {
      const eattributvariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eattributvariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Eattributvariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Eattributvariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Eattributvariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEattributvariableToCollectionIfMissing', () => {
      it('should add a Eattributvariable to an empty array', () => {
        const eattributvariable: IEattributvariable = sampleWithRequiredData;
        expectedResult = service.addEattributvariableToCollectionIfMissing([], eattributvariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eattributvariable);
      });

      it('should not add a Eattributvariable to an array that contains it', () => {
        const eattributvariable: IEattributvariable = sampleWithRequiredData;
        const eattributvariableCollection: IEattributvariable[] = [
          {
            ...eattributvariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEattributvariableToCollectionIfMissing(eattributvariableCollection, eattributvariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Eattributvariable to an array that doesn't contain it", () => {
        const eattributvariable: IEattributvariable = sampleWithRequiredData;
        const eattributvariableCollection: IEattributvariable[] = [sampleWithPartialData];
        expectedResult = service.addEattributvariableToCollectionIfMissing(eattributvariableCollection, eattributvariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eattributvariable);
      });

      it('should add only unique Eattributvariable to an array', () => {
        const eattributvariableArray: IEattributvariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eattributvariableCollection: IEattributvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEattributvariableToCollectionIfMissing(eattributvariableCollection, ...eattributvariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eattributvariable: IEattributvariable = sampleWithRequiredData;
        const eattributvariable2: IEattributvariable = sampleWithPartialData;
        expectedResult = service.addEattributvariableToCollectionIfMissing([], eattributvariable, eattributvariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eattributvariable);
        expect(expectedResult).toContain(eattributvariable2);
      });

      it('should accept null and undefined values', () => {
        const eattributvariable: IEattributvariable = sampleWithRequiredData;
        expectedResult = service.addEattributvariableToCollectionIfMissing([], null, eattributvariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eattributvariable);
      });

      it('should return initial array if no Eattributvariable is added', () => {
        const eattributvariableCollection: IEattributvariable[] = [sampleWithRequiredData];
        expectedResult = service.addEattributvariableToCollectionIfMissing(eattributvariableCollection, undefined, null);
        expect(expectedResult).toEqual(eattributvariableCollection);
      });
    });

    describe('compareEattributvariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEattributvariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEattributvariable(entity1, entity2);
        const compareResult2 = service.compareEattributvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEattributvariable(entity1, entity2);
        const compareResult2 = service.compareEattributvariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEattributvariable(entity1, entity2);
        const compareResult2 = service.compareEattributvariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
